package com.example.booking.domain.queue;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RedisToken {
    private final String uuid; // UUID
    private final Long userId; // 사용자 ID
    private final double timestamp; // 생성된 시간 또는 대기열에 추가된 시간 (zset때 score라는 실수라서 double)

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

}
