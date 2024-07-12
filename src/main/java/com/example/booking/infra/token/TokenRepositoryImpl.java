package com.example.booking.infra.token;

import com.example.booking.domain.queue.Token;
import com.example.booking.domain.queue.TokenRepository;
import com.example.booking.infra.token.entity.TokenEntity;
import com.example.booking.infra.token.entity.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final TokenJpaRepository tokenJpaRepository;

    @Override
    public Token save(Token token) {
        return tokenJpaRepository.save(TokenEntity.from(token)).toModel();
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenJpaRepository.findByToken(token).map(TokenEntity::toModel);
    }

    @Override
    public List<Token> findActiveTokens() {
        return tokenJpaRepository.findByStatus(TokenStatus.ACTIVE).stream()
                .map(TokenEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public int countByStatus(TokenStatus status) {
        return tokenJpaRepository.countByStatus(status);
    }

    @Override
    public List<Token> findTopByStatusOrderByCreatedAt(TokenStatus status, int n) {
        return tokenJpaRepository.findTopNByStatusOrderByCreatedAt(status, n).stream()
                .map(TokenEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Token> findAllByOrderByCreatedAt() {
        return tokenJpaRepository.findAllByOrderByCreatedAt().stream()
                .map(TokenEntity::toModel)
                .collect(Collectors.toList());
    }
}
