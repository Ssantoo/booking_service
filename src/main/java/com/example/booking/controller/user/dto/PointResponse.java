package com.example.booking.controller.user.dto;

import com.example.booking.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointResponse {

    private final long userId;
    private final int point;

    public PointResponse(long userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public static PointResponse from(User user) {
        return PointResponse.builder()
                .userId(user.getId())
                .point(user.getPoint())
                .build();
    }


}
