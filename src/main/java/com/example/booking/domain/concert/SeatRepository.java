package com.example.booking.domain.concert;

import java.util.List;

public interface SeatRepository {
    List<Seat> findByScheduleId(long scheduleId);
}
