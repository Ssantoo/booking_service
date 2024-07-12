package com.example.booking.domain.queue;

import com.example.booking.common.exception.UserNotFoundException;
import com.example.booking.infra.token.entity.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    private static final int MAX_ACTIVE_USERS = 100;

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

    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void activateQueuedUsers() {
        int activeUsers = tokenRepository.countByStatus(TokenStatus.ACTIVE);
        if (activeUsers < MAX_ACTIVE_USERS) {
            List<Token> queuedUsers = tokenRepository.findTopByStatusOrderByCreatedAt(TokenStatus.WAITING, MAX_ACTIVE_USERS - activeUsers);
            queuedUsers.stream()
                    .map(Token::activate)
                    .forEach(tokenRepository::save);
        }
    }

    public int getUserPositionInQueue(Long userId) {
        List<Token> queue = tokenRepository.findAllByOrderByCreatedAt();
        return Token.getUserPositionInQueue(queue, userId);
    }

}
