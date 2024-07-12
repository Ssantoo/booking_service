package com.example.booking.domain.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token generate(Long userId) {

        final Token token = Token.generate(userId);
        final Token createdToken = tokenRepository.save(token);
        return createdToken;
    }

    public void expireTokens() {
        List<Token> activeTokens = tokenRepository.findActiveTokens();

        activeTokens.stream()
                .filter(Token::isExpired)
                .map(Token::expire)
                .forEach(tokenRepository::save);
    }

    public void expireInactiveActiveTokens() {

        List<Token> activeTokens = tokenRepository.findActiveTokens();

        activeTokens.stream()
                .filter(token -> token.isInactiveForDuration(Duration.ofMinutes(10)))
                .map(Token::expire)
                .forEach(tokenRepository::save);
    }
}
