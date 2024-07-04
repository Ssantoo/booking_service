package com.example.booking.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargeRequest {
    private long userId;
    private int amount;

    public ChargeRequest(long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
