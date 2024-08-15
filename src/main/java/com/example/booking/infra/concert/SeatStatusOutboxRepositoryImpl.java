package com.example.booking.infra.concert;

import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.event.SeatStatusOutbox;
import com.example.booking.domain.event.SeatStatusOutboxRepository;
import com.example.booking.infra.concert.entity.SeatStatusOutboxEntity;
import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatStatusOutboxRepositoryImpl implements SeatStatusOutboxRepository {

    private final SeatStatusOutboxJpaRepository seatStatusOutboxJpaRepository;


    @Override
    public SeatStatusOutbox save(SeatStatusOutbox seatStatusOutbox) {
        return seatStatusOutboxJpaRepository.save(SeatStatusOutboxEntity.from(seatStatusOutbox)).toModel();
    }

    @Override
    public Optional<SeatStatusOutbox> findById(Long outboxId) {
        return seatStatusOutboxJpaRepository.findById(outboxId).map(SeatStatusOutboxEntity::toModel);
    }
}
