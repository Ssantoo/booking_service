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

    private final Seat seat;

    private ReservationStatus status;

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private final int totalPrice;

    public Reservation(Long id, Long userId, Long concertScheduleId, Seat seat, ReservationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.concertScheduleId = concertScheduleId;
        this.seat = seat;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalPrice = totalPrice;
    }

    // 상태 변경 메서드
    public void hold() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("예약할 수 없는 상태입니다.");
        }
        this.status = ReservationStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("확인할 수 없는 상태입니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("취소할 수 없는 상태입니다.");
        }
        this.status = ReservationStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getSeatId() {
        return this.seat.getId();
    }

}
