package com.example.booking.domain.concert;

import com.example.booking.infra.concert.entity.ScheduleEntity;

import java.util.List;

public interface ScheduleRepository {
    List<Schedule> findByConcertId(long concertId);

    void deleteAll();

    void save(ScheduleEntity schedule);
}
