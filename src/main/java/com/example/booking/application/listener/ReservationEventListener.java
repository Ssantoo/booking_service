package com.example.booking.application.listener;

import com.example.booking.domain.event.ReservationEvent;
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

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void sendReservationInfo(ReservationEvent event) {
        reservationMockApiClient.sendData(event.getReservation());
    }
}
