package com.example.booking.infra.concert;

import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.concert.SeatRepository;
import com.example.booking.infra.concert.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public List<Seat> findByScheduleId(long scheduleId) {
        return seatJpaRepository.findByScheduleId(scheduleId).stream().map(SeatEntity::toModel).collect(Collectors.toList());
    }
}
