package com.example.booking.domain.queue;

public interface RedisQueueRepository {
    RedisToken addToQueue(RedisToken token, String tokenValue);

    Long getPositionInQueue(String tokenValue);
}
