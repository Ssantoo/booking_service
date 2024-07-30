package com.example.booking.domain.queue;

import com.example.booking.infra.token.entity.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    public Token generate(Long userId) {

        final Token token = Token.generate(userId);
        final Token createdToken = queueRepository.save(token);
        return createdToken;
    }

    public void expireTokens() {
        //db에서 created_at으로부터 5분 지난 token값 들고오기
        List<Token> expireToken = queueRepository.findTokenActiveWithinLast5Minutes();
        List<Token> token = expireToken.stream().map(Token::expire).toList();
        queueRepository.saveAll(token);
    }

    public Optional<Token> findToken(String token) {
        return queueRepository.findToken(token);
    }

    public void activateQueuedUsers() {
        int activeTokenCount = queueRepository.countByStatus(TokenStatus.ACTIVE);
        if (Queue.hasAvailableSlot(activeTokenCount)) {
            List<Token> targetTokens = queueRepository.findLatestWatingTokenByCount(TokenStatus.WAITING, Queue.countAvailableSlot(activeTokenCount));
            //List<Token> targetToken = targetTokens.stream().map(Token::activate).toList();
            //queueRepository.saveAll(targetToken);

        }
    }

    public int getUserPositionInQueue(Long userId) {
        List<Token> queue = queueRepository.findAllByOrderByCreatedAt();
        return Token.getUserPositionInQueue(queue, userId);
    }

    public Optional<Token> findByToken(String token) {
        return queueRepository.findByToken(token);
    }
}
