package com.example.booking.infra.token;

import com.example.booking.domain.queue.Token;
import com.example.booking.domain.queue.QueueRepository;
import com.example.booking.infra.token.entity.TokenEntity;
import com.example.booking.infra.token.entity.TokenStatus;
import com.example.booking.infra.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public Token save(Token token) {
        return queueJpaRepository.save(TokenEntity.from(token)).toModel();
    }

    @Override
    public Optional<Token> findToken(String token) {
        return queueJpaRepository.findByToken(token).map(TokenEntity::toModel);
    }

    @Override
    public List<Token> findActiveTokens() {
        return queueJpaRepository.findByStatus(TokenStatus.ACTIVE).stream()
                .map(TokenEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public int countByStatus(TokenStatus status) {
        return queueJpaRepository.countByStatus(status);
    }

    @Override
    public List<Token> findTopByStatusOrderByCreatedAt(TokenStatus status, int n) {
        return queueJpaRepository.findTopNByStatusOrderByCreatedAt(status, n).stream()
                .map(TokenEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Token> findAllByOrderByCreatedAt() {
        return queueJpaRepository.findAllByOrderByCreatedAt().stream()
                .map(TokenEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Token> findTokenActiveWithinLast5Minutes() {
        return queueJpaRepository.findActiveWithinLastMinutes().stream()
                .map(TokenEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Token> targetToken) {
        queueJpaRepository.saveAll(targetToken.stream()
                .map(TokenEntity::from)
                .collect(Collectors.toList()));
    }
}
