package com.example.booking.application.listener;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.event.ReservationEvent;
import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentService;
import com.example.booking.domain.user.User;
import com.example.booking.infra.concert.ReservationMockApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final ReservationMockApiClient reservationMockApiClient;
    private final PaymentService paymentService;
    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void sendReservationInfo(ReservationEvent event) {
        Reservation reservation = event.getReservation();
        User user = event.getUser();
        try {
            
            reservationMockApiClient.sendData(reservation, user);

            // 결제 로직 실행
            paymentService.pay(user, reservation);
        } catch (Exception e) {
            // 복구 로직 처리
            throw e;
        }
    }
}
