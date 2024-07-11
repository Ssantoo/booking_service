package com.example.booking.domain.concert;

import com.example.booking.infra.concert.entity.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class Reservation {

    private final Long id;

    private final Long userId;

    private final Long concertScheduleId;

    private final Long seatId;

    private final ReservationStatus status;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final int totalPrice;

    public Reservation(Long id, Long userId, Long concertScheduleId, Long seatId, ReservationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.concertScheduleId = concertScheduleId;
        this.seatId = seatId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalPrice = totalPrice;
    }


}
