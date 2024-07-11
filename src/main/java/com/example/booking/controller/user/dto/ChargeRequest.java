package com.example.booking.controller.user.dto;

import com.example.booking.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargeRequest {
    private final long userId;
    private final int amount;

    public ChargeRequest(long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public User toDomain(User user){
        return User.builder()
                .id(this.userId)
                .name(user.getName())
                .point(this.amount)
                .build();
    }
}
