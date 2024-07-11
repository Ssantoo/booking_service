package com.example.booking.controller.concert.dto;

import com.example.booking.domain.concert.Schedule;
import com.example.booking.domain.concert.Seat;
import com.example.booking.infra.concert.entity.SeatStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class SeatResponse {

    private final Long id;
    private final int seatNuamber;
    private final int price;
    private final SeatStatus status;

    public SeatResponse(Long id, int seatNumber, int price, SeatStatus status) {
        this.id = id;
        this.seatNuamber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getPrice(),
                seat.getStatus()
        );
    }

    public static List<SeatResponse> from(List<Seat> seats) {
        return seats.stream().map(SeatResponse::from).collect(Collectors.toList());
    }
}
