package com.example.booking.domain.queue;

public interface RedisQueueRepository {
    void addToQueue(RedisToken token, String tokenValue);
}
