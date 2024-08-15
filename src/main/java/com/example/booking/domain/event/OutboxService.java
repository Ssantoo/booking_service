package com.example.booking.domain.event;

import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final ReservationOutboxRepository reservationOutboxRepository;

    private final SeatStatusOutboxRepository seatStatusOutboxRepository;

    public void toRetry(Long outboxId) {
    }

    public ReservationOutbox save(ReservationEvent event) {
        String jsonData = String.valueOf(event.getReservation());
        return reservationOutboxRepository.save(ReservationOutbox.create(jsonData));
    }

    public ReservationOutbox findByReservationId(Long reservationId) {
        return reservationOutboxRepository.findByReservationId(reservationId);
    }

    public void updateStatus(Long outboxId) {
        ReservationOutbox outbox = reservationOutboxRepository.findById(outboxId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox 찾을수 없습니다 " + outboxId));
        outbox.update();
        reservationOutboxRepository.save(outbox);
    }
}
