package com.example.booking.domain.event;

import java.util.Optional;

public interface SeatStatusOutboxRepository {
    SeatStatusOutbox save(SeatStatusOutbox seatStatusOutbox);

    Optional<SeatStatusOutbox> findById(Long outboxId);
}
