package com.example.booking.controller.reservation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class ReservationRequest {
    private long concertId;
    private LocalDateTime date;
    private int seatNumber;

    public ReservationRequest(long concertId, LocalDateTime date, int seatNumber) {
        this.concertId = concertId;
        this.date = date;
        this.seatNumber = seatNumber;
    }
}
