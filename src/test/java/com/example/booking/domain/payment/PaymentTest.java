package com.example.booking.domain.payment;

import com.example.booking.domain.concert.Concert;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.Schedule;
import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.user.User;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class PaymentTest {

    @Test
    public void 결제_도메인_테스트() {
        User mockUser = new User(1L, "testUser", 1000);

        Schedule mockSchedule = Schedule.builder().id(1L).dateTime(LocalDateTime.now()).totalSeats(100).availableSeats(50).concert(new Concert(1L, "해리포터 1", "마법사의돌")).build();
        Seat mockSeat = Seat.builder()
                .id(1L)
                .seatNumber(1)
                .price(100)
                .status(SeatStatus.AVAILABLE)
                .schedule(mockSchedule)
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

        Payment payment = Payment.pay(mockUser, mockReservation);

        assertNotNull(payment);
        assertEquals(mockUser, payment.getUserId());
        assertEquals(mockReservation, payment.getReservationId());
        assertEquals(100, payment.getAmount());
        assertEquals(PaymentStatus.CONFIRMED, payment.getStatus());
        assertNotNull(payment.getPaymentTime());
    }


}

