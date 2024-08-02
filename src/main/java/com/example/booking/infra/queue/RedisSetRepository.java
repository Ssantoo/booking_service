package com.example.booking.infra.queue;

import com.example.booking.domain.queue.RedisToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisSetRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // 대기열 추가
    public RedisToken addToQueue(RedisToken token, String tokenValue) {
        redisTemplate.opsForZSet().add(RedisToken.getQueueKey(), tokenValue, token.getTimestamp());
        return token;
    }

    // 순서 체크
    public Long getPositionInQueue(String tokenValue) {
        return redisTemplate.opsForZSet().rank(RedisToken.getQueueKey(), tokenValue);
    }

    // 토큰을 활성화 상태로 설정
    public void activateUser(RedisToken token) {
        String activeKey = token.getActiveUserKey();
        redisTemplate.opsForValue().set(activeKey, token.getUserId().toString(), 6, TimeUnit.MINUTES);
    }

    // 대기열에서 제거
    public void removeFromQueue(String tokenValue) {
        redisTemplate.opsForZSet().remove(RedisToken.getQueueKey(), tokenValue);
    }

    // 활성 사용자로 변경
    public void activateUsers(int count) {
        Set<String> tokens = redisTemplate.opsForZSet().range(RedisToken.getQueueKey(), 0, count - 1);
        if (tokens != null) {
            tokens.forEach(tokenValue -> {
                RedisToken token = RedisToken.parseTokenValue(tokenValue);
                activateUser(token);
                removeFromQueue(tokenValue);
            });
        }
    }

    // 토큰이 활성화 상태인지 확인
    public boolean isActive(RedisToken token) {
        String activeKey = token.getActiveUserKey();
        return Boolean.TRUE.equals(redisTemplate.hasKey(activeKey));
    }

    // 활성화된 토큰 인원수를 카운트
    public int countActiveTokens() {
        Set<String> activeKeys = redisTemplate.keys(RedisToken.getActiveUserPrefix() + "*");
        return activeKeys != null ? activeKeys.size() : 0;
    }

    // 활성 토큰 만료 처리
    public void handleExpiredTokens() {
        Set<String> activeKeys = redisTemplate.keys(RedisToken.getActiveUserPrefix() + "*");
        if (activeKeys != null) {
            activeKeys.forEach(activeKey -> {
                if (!Boolean.TRUE.equals(redisTemplate.hasKey(activeKey))) {
                    String[] parts = activeKey.split(":");
                    String uuid = parts[1];
                    Long userId = Long.parseLong(parts[2]);
                    RedisToken token = RedisToken.builder()
                            .uuid(uuid)
                            .userId(userId)
                            .timestamp(System.currentTimeMillis())
                            .build();
                    String tokenValue = token.getTokenValue();
                    addToQueue(token, tokenValue);
                }
            });
        }
    }


    public void delete(String activeKey) {
        redisTemplate.delete(activeKey);
    }
}



