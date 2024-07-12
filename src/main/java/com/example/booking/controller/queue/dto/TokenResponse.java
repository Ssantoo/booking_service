package com.example.booking.controller.queue.dto;

import com.example.booking.infra.token.entity.TokenStatus;
import lombok.Builder;
import lombok.Getter;
import com.example.booking.domain.queue.Token;

import java.time.LocalDateTime;

@Getter
@Builder
public class TokenResponse {
    private final Long userId;
    private final String token;
    private final LocalDateTime expiresAt;
    private final TokenStatus status;

    public TokenResponse(Long userId, String token, LocalDateTime expiresAt, TokenStatus status) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.status = status;
    }


    public static TokenResponse from(Token token) {
        return TokenResponse.builder()
                .userId(token.getUserId())
                .token(token.getToken())
                .expiresAt(token.getExpiredAt())
                .status(token.getStatus())
                .build();
    }
}
