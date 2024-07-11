package com.example.booking.infra.concert;

import com.example.booking.infra.concert.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByConcertId(long concertId);
}
