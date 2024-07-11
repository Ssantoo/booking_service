package com.example.booking.domain.concert;

import java.util.List;

public interface ScheduleRepository {
    List<Schedule> findByConcertId(long concertId);
}
