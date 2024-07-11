package com.example.booking.infra.concert;

import com.example.booking.domain.concert.Schedule;
import com.example.booking.domain.concert.ScheduleRepository;
import com.example.booking.infra.concert.entity.ScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public List<Schedule> findByConcertId(long concertId) {
        return scheduleJpaRepository.findByConcertId(concertId).stream()
                .map(ScheduleEntity::toModel)
                .collect(Collectors.toList());
    }
}
