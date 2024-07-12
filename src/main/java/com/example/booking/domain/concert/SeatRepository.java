package com.example.booking.domain.concert;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findByScheduleId(long scheduleId);

    Optional<Seat> findById(Long seatId);

    void save(Seat seat);

    Optional<Seat> findByIdForUpdate(Long seatId);
}
