package com.example.booking.domain.queue;

import com.example.booking.infra.queue.RedisSetRepository;
import com.example.booking.infra.token.entity.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisQueueService {

    private final RedisSetRepository redisSetRepository;

    public RedisToken generate(Long userId) {
        RedisToken token = RedisToken.createToken(userId);
        String tokenValue = token.getTokenValue();

        int activeUserCount = getActiveUserCount();

        if (RedisToken.hasAvailableSlot(activeUserCount)) {
            redisSetRepository.activateUser(token);
        } else {
            redisSetRepository.addToQueue(token, tokenValue);
        }

        return token;
    }

    public Long getQueuePosition(String tokenValue) {

        int activeUserCount = getActiveUserCount();
        int availableSlots = RedisToken.countAvailableSlot(activeUserCount);

        if (availableSlots > 0) {
            RedisToken token = RedisToken.parseTokenValue(tokenValue);
            redisSetRepository.activateUser(token);
            redisSetRepository.removeFromQueue(tokenValue);
            return 0L;
        }

        Long position = redisSetRepository.getPositionInQueue(tokenValue);
        return position == null ? null : position + 1;  // zindex가 0부터 시작하니까
    }

    // 활성 사용자 관리
    public void activateUsers() {
        int activeUserCount = redisSetRepository.countActiveTokens();
        int availableSlots = RedisToken.countAvailableSlot(activeUserCount);

        if (availableSlots > 0) {
            redisSetRepository.activateUsers(availableSlots);
        }
    }

    // 활성 사용자 수를 반환
    public int getActiveUserCount() {
        return redisSetRepository.countActiveTokens();
    }

    // 만료된 토큰 처리
    public void handleExpiredTokens() {
        redisSetRepository.handleExpiredTokens();
    }

    // 특정 사용자의 활성 토큰 만료
    public void expireToken(RedisToken token) {
        String activeKey = token.getActiveUserKey();
        redisSetRepository.delete(activeKey);
    }

    // 토큰이 활성 상태인지 확인
    public boolean isActive(String tokenValue) {
        RedisToken token = RedisToken.parseTokenValue(tokenValue);
        return redisSetRepository.isActive(token);
    }
}

