package com.example.booking.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String token;
    private int queueNo;
    private int expiresIn;

    public TokenResponse(String token, int queueNo, int expiresIn) {
        this.token = token;
        this.queueNo = queueNo;
        this.expiresIn = expiresIn;
    }


}
