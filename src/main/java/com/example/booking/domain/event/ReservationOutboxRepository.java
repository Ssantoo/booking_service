package com.example.booking.domain.event;

import java.util.Optional;

public interface ReservationOutboxRepository {
    ReservationOutbox save(ReservationOutbox reservationOutbox);

    ReservationOutbox findByReservationId(Long reservationId);

    Optional<ReservationOutbox> findById(Long outboxId);
}
