package com.example.booking.infra.concert;

import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.event.SeatStatusOutbox;
import com.example.booking.domain.event.SeatStatusOutboxRepository;
import com.example.booking.infra.concert.entity.ScheduleEntity;
import com.example.booking.infra.concert.entity.SeatStatusOutboxEntity;
import com.example.booking.infra.concert.entity.SeatStatusOutboxStatus;
import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<SeatStatusOutbox> findAllByStatus(SeatStatusOutboxStatus seatStatusOutboxStatus) {
        return seatStatusOutboxJpaRepository.findAllByStatus(seatStatusOutboxStatus).stream()
                .map(SeatStatusOutboxEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementSeatStatus(SeatStatusOutbox outbox) {
        seatStatusOutboxJpaRepository.save(SeatStatusOutboxEntity.from(outbox));
    }
}
