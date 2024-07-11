package com.example.booking.controller.concert.dto;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.infra.concert.entity.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class ReservationRequest {
    private final String token;
    private final Long concertScheduleId;
    private final Long seatId;
    private final Long userId;

    public ReservationRequest(String token, Long concertScheduleId, Long seatId, Long userId ) {
        this.token = token;
        this.concertScheduleId = concertScheduleId;
        this.seatId = seatId;
        this.userId = userId;
    }

    public Reservation toDomain() {
        return Reservation.builder()
                .concertScheduleId(this.concertScheduleId)
                .seatId(this.seatId)
                .userId(this.userId)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
