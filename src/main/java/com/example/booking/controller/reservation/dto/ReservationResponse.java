package com.example.booking.controller.reservation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationResponse {
    private String status;
    private int seatNumber;
    private int holdTime;

    public ReservationResponse(String status, int seatNumber, int holdTime) {
        this.status = status;
        this.seatNumber = seatNumber;
        this.holdTime = holdTime;
    }
}
