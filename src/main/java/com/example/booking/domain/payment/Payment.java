package com.example.booking.domain.payment;

import com.example.booking.domain.concert.Reservation;
import lombok.Builder;
import lombok.Getter;
import com.example.booking.domain.user.User;
import java.time.LocalDateTime;

@Getter
@Builder
public class Payment {

    private final User userId;
    private final Reservation reservationId;
    private final int amount;
    private final LocalDateTime paymentTime;
    private final PaymentStatus status;

    public Payment(User userId, Reservation reservationId, int amount, LocalDateTime paymentTime, PaymentStatus status) {

        this.userId = userId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentTime = paymentTime;
        this.status = status;
    }

    public static Payment pay(User user, Reservation reservation) {
        return new Payment(
                user,
                reservation,
                reservation.getTotalPrice(),
                LocalDateTime.now(),
                PaymentStatus.CONFIRMED
        );
    }
}
