package com.example.booking.domain.queue;

import com.example.booking.common.exception.NotReservableException;
import com.example.booking.common.exception.UserNotFoundException;
import com.example.booking.infra.token.entity.TokenStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Token {
    private final Long id;
    private final Long userId;
    private final String token;
    private final LocalDateTime expiredAt;
    private final TokenStatus status;
    private final LocalDateTime lastActivityAt;
    private final LocalDateTime createdAt;

    public static Token generate(Long userId) {
        String tokenString = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        return Token.builder()
                .userId(userId)
                .token(tokenString)
                .expiredAt(expiresAt)
                .status(TokenStatus.WAITING)
                .lastActivityAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public Token expire() {
        return Token.builder()
                .id(id)
                .userId(userId)
                .token(token)
                .expiredAt(expiredAt)
                .status(TokenStatus.EXPIRED)
                .lastActivityAt(lastActivityAt)
                .createdAt(createdAt)
                .build();
    }

    public Token activate() {
        return Token.builder()
                .id(id)
                .userId(userId)
                .token(token)
                .expiredAt(expiredAt)
                .status(TokenStatus.ACTIVE)
                .lastActivityAt(LocalDateTime.now())
                .createdAt(createdAt)
                .build();
    }

    public Token updateLastActivity() {
        return Token.builder()
                .id(id)
                .userId(userId)
                .token(token)
                .expiredAt(expiredAt)
                .status(status)
                .lastActivityAt(LocalDateTime.now())
                .createdAt(createdAt)
                .build();
    }

    public boolean isInactiveForDuration(Duration duration) {
        return lastActivityAt.isBefore(LocalDateTime.now().minus(duration));
    }

    public void validateActive() {
        if (this.status != TokenStatus.ACTIVE || this.isExpired()) {
            throw new NotReservableException("토큰 상태를 확인해주세요.");
        }
    }

    public static int getUserPositionInQueue(List<Token> queue, Long userId) {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getUserId().equals(userId)) {
                return i + 1;
            }
        }
        throw new UserNotFoundException("유저를 찾을 수 없습니다");
    }
}
