package com.example.booking.domain.payment;

import com.example.booking.domain.concert.Concert;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.Schedule;
import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    @Test
    public void 결제_성공() {
        Schedule mockSchedule = Schedule.builder().id(1L).dateTime(LocalDateTime.now()).totalSeats(100).availableSeats(50).concert(new Concert(1L, "해리포터 1", "마법사의돌")).build();
        Seat mockSeat = Seat.builder()
                .id(1L)
                .seatNumber(1)
                .price(100)
                .status(SeatStatus.AVAILABLE)
                .schedule(mockSchedule)
                .build();

        User mockUser = new User(1L, "조현재", 1000);
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

        User updatedUser = mockUser.use(100);

        given(userRepository.findByIdWithLock(1L)).willReturn(mockUser);

        Payment mockPayment = Payment.pay(updatedUser, mockReservation);
        given(paymentRepository.save(any(Payment.class))).willReturn(mockPayment);

        Payment result = paymentService.pay(mockUser, mockReservation);

        assertNotNull(result);
        assertEquals(PaymentStatus.CONFIRMED, result.getStatus());
        assertEquals(100, result.getAmount());

        verify(userRepository, times(1)).findByIdWithLock(1L);
        verify(userRepository, times(1)).save(userCaptor.capture());
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        User capturedUser = userCaptor.getValue();
        Payment capturedPayment = paymentCaptor.getValue();

        assertEquals(900, capturedUser.getPoint());
        assertEquals(mockReservation, capturedPayment.getReservationId());
    }

}
