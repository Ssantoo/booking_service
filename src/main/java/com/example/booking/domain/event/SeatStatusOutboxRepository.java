package com.example.booking.domain.event;

import com.example.booking.infra.concert.entity.SeatStatusOutboxStatus;

import java.util.List;
import java.util.Optional;

public interface SeatStatusOutboxRepository {
    SeatStatusOutbox save(SeatStatusOutbox seatStatusOutbox);

    Optional<SeatStatusOutbox> findById(Long outboxId);

    List<SeatStatusOutbox> findAllByStatus(SeatStatusOutboxStatus seatStatusOutboxStatus);

    void incrementSeatStatus(SeatStatusOutbox outbox);
}
