package com.example.booking.domain.event;

import com.example.booking.infra.payment.entity.ReservationOutboxStatus;

import java.util.List;
import java.util.Optional;

public interface ReservationOutboxRepository {
    ReservationOutbox save(ReservationOutbox reservationOutbox);

    ReservationOutbox findByReservationId(Long reservationId);

    Optional<ReservationOutbox> findById(Long outboxId);

    List<ReservationOutbox> findAllByStatus(ReservationOutboxStatus reservationOutboxStatus);

    void incrementReservation(ReservationOutbox outbox);
}
