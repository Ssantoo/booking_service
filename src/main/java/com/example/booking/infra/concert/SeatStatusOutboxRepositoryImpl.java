package com.example.booking.infra.concert;

import com.example.booking.domain.event.SeatStatusOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeatStatusOutboxRepositoryImpl implements SeatStatusOutboxRepository {

    private final SeatStatusOutboxJpaRepository seatStatusOutboxJpaRepository;


}
