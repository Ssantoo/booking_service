package com.example.booking.infra.concert;

import com.example.booking.domain.event.SeatStatusOutbox;
import com.example.booking.infra.concert.entity.SeatStatusOutboxEntity;
import com.example.booking.infra.concert.entity.SeatStatusOutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatStatusOutboxJpaRepository extends JpaRepository<SeatStatusOutboxEntity, Long> {

    List<SeatStatusOutboxEntity> findAllByStatus(SeatStatusOutboxStatus seatStatusOutboxStatus);
}
