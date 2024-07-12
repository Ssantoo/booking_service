package com.example.booking.domain.queue;

import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    Token save(Token token);

    Optional<Token> findByToken(String token);
    List<Token> findActiveTokens();
}
