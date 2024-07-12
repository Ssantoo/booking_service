package com.example.booking.controller.payment.dto;

import com.example.booking.controller.concert.dto.ConcertResponse;
import com.example.booking.domain.concert.Concert;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {
    private final Reservation reservationId;
    private final PaymentStatus status;
    private final LocalDateTime paymentTime;

    public PaymentResponse(Reservation reservationId, PaymentStatus status, LocalDateTime paymentTime) {
        this.reservationId = reservationId;
        this.status = status;
        this.paymentTime = paymentTime;
    }

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getReservationId(),
                payment.getStatus(),
                payment.getPaymentTime()
        );
    }
}