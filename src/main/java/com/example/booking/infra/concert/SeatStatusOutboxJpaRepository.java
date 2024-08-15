package com.example.booking.infra.concert;

import com.example.booking.infra.concert.entity.SeatStatusOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatStatusOutboxJpaRepository extends JpaRepository<SeatStatusOutboxEntity, Long> {
}
