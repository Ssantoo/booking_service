package com.example.booking.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointResponse {

    private long userId;
    private int points;

    public PointResponse(long userId, int points) {
        this.userId = userId;
        this.points = points;
    }


}
