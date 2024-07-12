package com.example.booking.infra.token.entity;


import com.example.booking.domain.queue.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    @Column(nullable = false)
    private LocalDateTime lastActivityAt;

    public Token toModel() {
        return Token.builder()
                .id(id)
                .userId(userId)
                .token(token)
                .expiredAt(expiredAt)
                .status(status)
                .lastActivityAt(lastActivityAt)
                .build();
    }

    public static TokenEntity from(Token token) {
        return TokenEntity.builder()
                .id(token.getId())
                .userId(token.getUserId())
                .token(token.getToken())
                .expiredAt(token.getExpiredAt())
                .status(token.getStatus())
                .lastActivityAt(token.getLastActivityAt())
                .build();
    }
}
