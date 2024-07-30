package com.example.booking.controller.user.dto;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UseRequest {
    private final long userId;
    private final Reservation reservation;

    public UseRequest(long userId, Reservation reservation) {
        this.userId = userId;
        this.reservation = reservation;
    }

}
