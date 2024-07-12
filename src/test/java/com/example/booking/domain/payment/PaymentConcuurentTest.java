package com.example.booking.domain.payment;

import com.example.booking.application.PaymentFacade;
import com.example.booking.domain.concert.*;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.domain.user.UserService;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/reservation-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Import({PaymentFacade.class, PaymentService.class, UserService.class, ConcertService.class})
public class PaymentConcuurentTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Autowired
    private PaymentFacade paymentFacade;

    @Test
    public void 결제_동시성_테스트() throws Exception {
        User mockUser = new User(1L, "조현재", 1000);
        Seat mockSeat = Seat.builder()
                .id(1L)
                .seatNumber(1)
                .price(100)
                .status(SeatStatus.AVAILABLE)
                .schedule(Schedule.builder().id(1L).dateTime(LocalDateTime.now()).totalSeats(100).availableSeats(50).concert(new Concert(1L, "해리포터 1", "마법사의돌")).build())
                .build();

        Reservation mockReservation = Reservation.builder()
                .id(1L)
                .userId(1L)
                .concertScheduleId(1L)
                .seat(mockSeat)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(100)
                .build();

        // 동시성 테스트를 위해 두 개의 스레드에서 동시에 결제를 시도
        given(userRepository.findByIdWithLock(1L)).willReturn(mockUser);

        Payment mockPayment = Payment.pay(mockUser, mockReservation);
        given(paymentRepository.save(any(Payment.class))).willReturn(mockPayment);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Payment> future1 = executorService.submit(() -> {
            System.out.println("Thread 1: 결제 시작");
            Payment payment = paymentService.pay(mockUser, mockReservation);
            System.out.println("Thread 1: 결제 완료");
            return payment;
        });
        Future<Payment> future2 = executorService.submit(() -> {
            System.out.println("Thread 2: 결제 시작");
            Payment payment = paymentService.pay(mockUser, mockReservation);
            System.out.println("Thread 2: 결제 완료");
            return payment;
        });

        // 결제 요청이 완료될 때까지 기다리게하기
        Payment payment1 = future1.get(5, TimeUnit.SECONDS);
        Payment payment2 = future2.get(5, TimeUnit.SECONDS);

        System.out.println("Main thread: 결과 검증 시작");
        assertNotNull(payment1);
        assertEquals(PaymentStatus.CONFIRMED, payment1.getStatus());
        assertNotNull(payment2);
        assertEquals(PaymentStatus.CONFIRMED, payment2.getStatus());

        // 두 번의 결제 시도에서 포인트 차감이 발생했는지 검증하기
        verify(userRepository, times(2)).findByIdWithLock(1L);
        verify(userRepository, times(2)).save(any(User.class));
        verify(paymentRepository, times(2)).save(any(Payment.class));

        executorService.shutdown();
        System.out.println("Main thread: 테스트 완료");
    }

    @Test
    public void 결제_동시성_테스트2() throws Exception {
        User mockUser1 = new User(1L, "조현재", 1000);
        User mockUser2 = new User(1L, "조현재", 1000);
        Seat mockSeat = Seat.builder()
                .id(1L)
                .seatNumber(1)
                .price(100)
                .status(SeatStatus.AVAILABLE)
                .schedule(Schedule.builder().id(1L).dateTime(LocalDateTime.now()).totalSeats(100).availableSeats(50).concert(new Concert(1L, "해리포터 1", "마법사의돌")).build())
                .build();

        Reservation mockReservation = Reservation.builder()
                .id(1L)
                .userId(1L)
                .concertScheduleId(1L)
                .seat(mockSeat)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(100)
                .build();

        // 동시성 테스트를 위해 두 개의 스레드에서 동시에 결제를 시도
        given(userRepository.findByIdWithLock(1L)).willReturn(mockUser1, mockUser2);

        // Payment 생성 시마다 잔액을 업데이트하도록 모킹
        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            System.out.println("User 잔액" + savedUser.getPoint());
            return savedUser;
        });

        Payment mockPayment = Payment.pay(mockUser1, mockReservation);
        given(paymentRepository.save(any(Payment.class))).willReturn(mockPayment);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Payment> future1 = executorService.submit(() -> {
            System.out.println("Thread 1- 결제 시작");
            Payment payment = paymentService.pay(mockUser1, mockReservation);
            System.out.println("Thread 1- 결제 완료, 잔액: " + payment.getUserId().getPoint());
            return payment;
        });
        Future<Payment> future2 = executorService.submit(() -> {
            System.out.println("Thread 2- 결제 시작");
            Payment payment = paymentService.pay(mockUser2, mockReservation);
            System.out.println("Thread 2- 결제 완료, 잔액: " + payment.getUserId().getPoint());
            return payment;
        });

        // 결제 요청이 완료될 때까지 기다리게 하기
        Payment payment1 = future1.get(5, TimeUnit.SECONDS);
        Payment payment2 = future2.get(5, TimeUnit.SECONDS);

        System.out.println("Main thread- 결과 검증 시작");
        assertNotNull(payment1);
        assertEquals(PaymentStatus.CONFIRMED, payment1.getStatus());
        assertNotNull(payment2);
        assertEquals(PaymentStatus.CONFIRMED, payment2.getStatus());

        // 두 번의 결제 시도에서 포인트 차감이 발생했는지 검증하기
        verify(userRepository, times(2)).findByIdWithLock(1L);
        verify(userRepository, times(2)).save(any(User.class));
        verify(paymentRepository, times(2)).save(any(Payment.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(userCaptor.capture());

        // 모든 User 객체를 확인하여 포인트가 올바르게 차감되었는지 확인
        for (User savedUser : userCaptor.getAllValues()) {
            System.out.println("User 잔액" + savedUser.getPoint());
        }

        executorService.shutdown();
        System.out.println("Main thread- 테스트 완료");
    }


}
