package com.example.booking.domain.queue;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class RedisToken {
    private final String uuid; // UUID
    private final Long userId; // 사용자 ID
    private final double timestamp; // 생성된 시간 또는 대기열에 추가된 시간 (zset때 score라는 실수라서 double)


    private static final String QUEUE_KEY = "WAIT";

    private static final String ACTIVE_USER_PREFIX = "ACTIVE:";

    private static final int MAX_ACTIVE_USERS = 10;

    public static RedisToken createToken(long userId) {
        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        return RedisToken.builder()
                .uuid(uuid)
                .userId(userId)
                .timestamp((double) timestamp)
                .build();
    }


    public String getTokenValue() {
        return uuid + ":" + userId;
    }

    public static String getQueueKey() {
        return QUEUE_KEY;
    }

    public static String getActiveUserPrefix() {
        return ACTIVE_USER_PREFIX;
    }

    public String getActiveUserKey() {
        return ACTIVE_USER_PREFIX + uuid + ":" + userId;
    }

    public static RedisToken parseTokenValue(String tokenValue) {
        String[] parts = tokenValue.split(":");
        String uuid = parts[0];
        Long userId = Long.parseLong(parts[1]);
        return RedisToken.builder()
                .uuid(uuid)
                .userId(userId)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static boolean hasAvailableSlot(int countActiveTokens) {
        return countActiveTokens < MAX_ACTIVE_USERS;
    }

    public static int countAvailableSlot(int countActiveTokens) {
        return MAX_ACTIVE_USERS - countActiveTokens;
    }
}

