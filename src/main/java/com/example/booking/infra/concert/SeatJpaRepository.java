package com.example.booking.infra.concert;

import com.example.booking.infra.concert.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findByScheduleId(long scheduleId);
}
