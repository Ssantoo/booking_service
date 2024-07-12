package com.example.booking.controller.concert.dto;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.Seat;
import com.example.booking.infra.concert.entity.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class ReservationRequest {
    private final String token;
    private final Long concertScheduleId;
    private final Seat seat;
    private final Long userId;

    public ReservationRequest(String token, Long concertScheduleId, Seat seat, Long userId ) {
        this.token = token;
        this.concertScheduleId = concertScheduleId;
        this.seat = seat;
        this.userId = userId;
    }

    public Reservation toDomain() {
        return Reservation.builder()
                .concertScheduleId(this.concertScheduleId)
                .seat(seat)
                .userId(this.userId)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
