package com.example.booking.integration;

import com.example.booking.domain.concert.*;
import com.example.booking.domain.queue.QueueService;
import com.example.booking.domain.queue.Token;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.domain.user.UserService;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import com.example.booking.support.exception.AlreadyOccupiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/reservation-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class ReservationTest {

    private static final Logger log = LoggerFactory.getLogger(TxTest.class);

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ConcertService concertService;

    @Mock
    private QueueService queueService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void 좌석예약_비관적락_테스트() throws InterruptedException {
        int numThreads = 100;
        int seatId = 6;
        String token = "valid-token";
        long scheduleId = 1L;

        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    Schedule mockSchedule = Schedule.builder()
                            .id(scheduleId)
                            .dateTime(LocalDateTime.of(2024, 7, 5, 0, 0))
                            .totalSeats(100)
                            .availableSeats(50)
                            .concert(new Concert(1L, "해리포터 1", "마법사의돌"))
                            .build();
                    Seat seat = new Seat(1L, seatId, 100, SeatStatus.AVAILABLE, mockSchedule);
                    Reservation mockReservation = Reservation.builder()
                            .id(1L)
                            .userId(1L)
                            .concertScheduleId(1L)
                            .seat(seat)
                            .status(ReservationStatus.PENDING)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .totalPrice(100)
                            .build();

                    // 예약 시도
                    concertService.reserve(mockReservation, token);
                    log.info("[Thread ID: {}] 예약 성공", Thread.currentThread().getId());
                } catch (AlreadyOccupiedException e) {
                    log.error("[Thread ID: {}] 이미 예약된 좌석", Thread.currentThread().getId());
                } catch (Exception ex) {
                    log.error("[Thread ID: {}] Exception :: {}", Thread.currentThread().getId(), ex.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        log.info("소요 시간: {} ms", (endTime - startTime));
    }

    @Test
    public void 예약시도_2회_1조회_시도를_통한_조회로_인한_비관적락_단점_파악 () throws InterruptedException {
        long seatId = 6;
        String token = "valid-token";
        long scheduleId = 1L;
        long userId = 1L;

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        long startTime = System.currentTimeMillis();

        // tx1: 좌석 예약 시도
        executorService.execute(() -> {
            try {
                Schedule mockSchedule = Schedule.builder()
                        .id(scheduleId)
                        .dateTime(LocalDateTime.of(2024, 7, 5, 0, 0))
                        .totalSeats(100)
                        .availableSeats(50)
                        .concert(new Concert(1L, "해리포터 1", "마법사의돌"))
                        .build();
                Seat seat = new Seat(1L, (int)seatId, 100, SeatStatus.AVAILABLE, mockSchedule);
                Reservation mockReservation = Reservation.builder()
                        .id(1L)
                        .userId(userId)
                        .concertScheduleId(scheduleId)
                        .seat(seat)
                        .status(ReservationStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .totalPrice(100)
                        .build();

                concertService.reserve(mockReservation, token);
                log.info("[Thread ID: {}] tx1 예약 성공", Thread.currentThread().getId());
            } catch (Exception ex) {
                log.error("[Thread ID: {}] tx1 Exception :: {}", Thread.currentThread().getId(), ex.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // tx2: 좌석 예약 시도
        executorService.execute(() -> {
            try {
                Thread.sleep(50);
                Schedule mockSchedule = Schedule.builder()
                        .id(scheduleId)
                        .dateTime(LocalDateTime.of(2024, 7, 5, 0, 0))
                        .totalSeats(100)
                        .availableSeats(50)
                        .concert(new Concert(1L, "해리포터 1", "마법사의돌"))
                        .build();
                Seat seat = new Seat(1L, (int)seatId, 100, SeatStatus.AVAILABLE, mockSchedule);
                Reservation mockReservation = Reservation.builder()
                        .id(2L)
                        .userId(userId)
                        .concertScheduleId(scheduleId)
                        .seat(seat)
                        .status(ReservationStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .totalPrice(100)
                        .build();

                concertService.reserve(mockReservation, token);
                log.info("[Thread ID: {}] tx2 예약 성공", Thread.currentThread().getId());
            } catch (Exception ex) {
                log.error("[Thread ID: {}] tx2 Exception :: {}", Thread.currentThread().getId(), ex.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // tx3: 예약 상태 조회 시도
        executorService.execute(() -> {
            try {
                Thread.sleep(300);
                long tx3StartTime = System.currentTimeMillis();
                log.info("시작 tx3 조회 : " + tx3StartTime);

                Reservation reservation = reservationRepository.findById(seatId);

                long tx3EndTime = System.currentTimeMillis();
                log.info("tx3 조회 걸린시간 " + (tx3EndTime - tx3StartTime) + " milliseconds.");
            } catch (Exception ex) {
                log.error("[Thread ID: {}] tx3 Exception :: {}", Thread.currentThread().getId(), ex.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        log.info("전체 소요 시간: {} ms", (endTime - startTime));
    }

    @Test
    public void 예약시도_낙관적락_테스트() throws InterruptedException {
        long seatId = 6;
        String token = "valid-token";
        long scheduleId = 1L;
        long userId = 1L;

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        long startTime = System.currentTimeMillis();

        // tx1: 좌석 예약 시도
        executorService.execute(() -> {
            try {
                Schedule mockSchedule = Schedule.builder()
                        .id(scheduleId)
                        .dateTime(LocalDateTime.of(2024, 7, 5, 0, 0))
                        .totalSeats(100)
                        .availableSeats(50)
                        .concert(new Concert(1L, "해리포터 1", "마법사의돌"))
                        .build();
                Seat seat = new Seat(1L, (int)seatId, 100, SeatStatus.AVAILABLE, mockSchedule);
                Reservation mockReservation = Reservation.builder()
                        .id(1L)
                        .userId(userId)
                        .concertScheduleId(scheduleId)
                        .seat(seat)
                        .status(ReservationStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .totalPrice(100)
                        .build();

                concertService.reserve(mockReservation, token);
                log.info("[Thread ID: {}] tx1 예약 성공", Thread.currentThread().getId());
            } catch (Exception ex) {
                log.error("[Thread ID: {}] tx1 Exception :: {}", Thread.currentThread().getId(), ex.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // tx2: 좌석 예약 시도
        executorService.execute(() -> {
            try {
                Thread.sleep(50); // tx1이 먼저 실행되도록 약간의 지연 추가
                Schedule mockSchedule = Schedule.builder()
                        .id(scheduleId)
                        .dateTime(LocalDateTime.of(2024, 7, 5, 0, 0))
                        .totalSeats(100)
                        .availableSeats(50)
                        .concert(new Concert(1L, "해리포터 1", "마법사의돌"))
                        .build();
                Seat seat = new Seat(1L, (int)seatId, 100, SeatStatus.AVAILABLE, mockSchedule);
                Reservation mockReservation = Reservation.builder()
                        .id(2L)
                        .userId(userId)
                        .concertScheduleId(scheduleId)
                        .seat(seat)
                        .status(ReservationStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .totalPrice(100)
                        .build();

                concertService.reserve(mockReservation, token);
                log.info("[Thread ID: {}] tx2 예약 성공", Thread.currentThread().getId());
            } catch (ObjectOptimisticLockingFailureException e) {
                log.error("[Thread ID: {}] tx2 OptimisticLockException :: {}", Thread.currentThread().getId(), e.getMessage());
            } catch (Exception ex) {
                log.error("[Thread ID: {}] tx2 Exception :: {}", Thread.currentThread().getId(), ex.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        log.info("전체 소요 시간: {} ms", (endTime - startTime));
    }


}
