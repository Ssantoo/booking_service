package com.example.booking.application.listener;

import com.example.booking.domain.concert.ConcertService;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.event.SeatStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class SeatStatusChangeEventListener {

    private final ConcertService concertService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleSeatStatusChangeEvent(SeatStatusChangeEvent event) {
        Reservation reservation = event.getReservation();
        Seat seat = reservation.getSeat();
        concertService.changeStatus(seat);
    }
}
