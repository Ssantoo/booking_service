package com.example.booking.domain.queue;

import com.example.booking.infra.token.entity.TokenStatus;

import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Token save(Token token);

    Optional<Token> findToken(String token);
    List<Token> findActiveTokens();

    int countByStatus(TokenStatus tokenStatus);

    List<Token> findTopByStatusOrderByCreatedAt(TokenStatus tokenStatus, int i);

    List<Token> findAllByOrderByCreatedAt();

    List<Token> findTokenActiveWithinLast5Minutes();

    void saveAll(List<Token> targetToken);

    Optional<Token> findByToken(String token);

    List<Token> findLatestWatingTokenByCount(TokenStatus tokenStatus, int i);
}
