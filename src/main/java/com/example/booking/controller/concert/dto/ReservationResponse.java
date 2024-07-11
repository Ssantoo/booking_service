package com.example.booking.controller.concert.dto;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.infra.concert.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationResponse {
    private Long reservationId;
    private Long concertScheduleId;
    private Long seatId;
    private Long userId;
    private ReservationStatus status;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getConcertScheduleId(),
                reservation.getSeatId(),
                reservation.getUserId(),
                reservation.getStatus()
        );
    }
}
