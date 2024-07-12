package com.example.booking.domain.queue;

import com.example.booking.infra.token.entity.TokenStatus;

import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    Token save(Token token);

    Optional<Token> findByToken(String token);
    List<Token> findActiveTokens();

    int countByStatus(TokenStatus tokenStatus);

    List<Token> findTopByStatusOrderByCreatedAt(TokenStatus tokenStatus, int i);

    List<Token> findAllByOrderByCreatedAt();
}
