package com.example.booking.application;

import com.example.booking.domain.concert.ConcertService;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentService;
import com.example.booking.domain.user.UserService;
import com.example.booking.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final UserService userService;
    private final ConcertService concertService;

    public Payment processPayment(Long userId, Long reservationId) {
        final User user = userService.getUserByIdWithLock(userId);
        final Reservation reservation = concertService.getReservationById(reservationId);
        final Payment payment = paymentService.pay(user, reservation);
        return payment;

    }

}
