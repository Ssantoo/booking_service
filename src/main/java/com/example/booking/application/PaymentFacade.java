package com.example.booking.application;

import com.example.booking.domain.concert.ConcertService;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentService;
import com.example.booking.domain.queue.RedisQueueService;
import com.example.booking.domain.queue.RedisToken;
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
    private final RedisQueueService redisQueueService;


    public Payment processPayment(Long userId, Long reservationId, String activeToken) {
        if (!redisQueueService.isActive(activeToken)) {
            throw new IllegalStateException("토큰이 활성 상태가 아닙니다.");
        }
        final User user = userService.getUserById(userId);
        final Reservation reservation = concertService.getReservationById(reservationId);
        final Payment payment = paymentService.pay(user, reservation);
        RedisToken token = RedisToken.parseTokenValue(activeToken);
        redisQueueService.expireToken(token);
        return payment;
    }

}
