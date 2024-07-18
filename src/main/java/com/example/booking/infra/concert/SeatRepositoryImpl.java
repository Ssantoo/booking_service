package com.example.booking.infra.concert;

import com.example.booking.common.exception.ResourceNotFoundException;
import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.concert.SeatRepository;
import com.example.booking.infra.concert.entity.SeatEntity;
import com.example.booking.infra.concert.entity.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public List<Seat> findByScheduleId(long scheduleId) {
        return seatJpaRepository.findByScheduleId(scheduleId).stream().map(SeatEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Seat> findById(Long seatId) {
        return seatJpaRepository.findById(seatId).map(SeatEntity::toModel);
    }

    @Override
    public void save(Seat seat) {
        seatJpaRepository.save(SeatEntity.from(seat));
    }

    @Override
    public Optional<Seat> findByIdForUpdate(Long seatId) {
        return seatJpaRepository.findByIdForUpdate(seatId).map(SeatEntity::toModel);
    }

    @Override
    public List<Seat> findAvailableSeatsByScheduleId(long scheduleId, SeatStatus status) {
        return seatJpaRepository.findByScheduleIdAndAvailable(scheduleId, status).stream().map(SeatEntity::toModel).collect(Collectors.toList());
    }
}
