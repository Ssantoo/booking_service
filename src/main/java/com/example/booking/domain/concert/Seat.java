package com.example.booking.domain.concert;

import com.example.booking.infra.concert.entity.SeatStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Seat {

    private final Long id;
    private int seatNumber;
    private int price;
    private SeatStatus status;
    private Schedule schedule;
    private int version;

    public Seat(Long id, int seatNumber, int price, SeatStatus status, Schedule schedule, int version){
        this.id = id;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
        this.schedule = schedule;
        this.version = version;
    }


    public void hold() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("예약할 수 없는 좌석입니다.");
        }
        this.status = SeatStatus.HOLD;
    }

    public void reserve() {
        if (this.status != SeatStatus.HOLD) {
            throw new IllegalStateException("예약한 좌석이 아닙니다.");
        }
        this.status = SeatStatus.RESERVED;
    }

    public void cancelHold() {
        if (this.status != SeatStatus.HOLD) {
            throw new IllegalStateException("예약 되어있지 않는 좌석입니다.");
        }
        this.status = SeatStatus.AVAILABLE;
    }

    public void cancelReservation() {
        if (this.status != SeatStatus.RESERVED) {
            throw new IllegalStateException("예약 되어있지 않는 좌석입니다.");
        }
        this.status = SeatStatus.AVAILABLE;
    }



}
