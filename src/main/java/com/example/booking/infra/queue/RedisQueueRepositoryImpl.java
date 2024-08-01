package com.example.booking.infra.queue;

import com.example.booking.domain.queue.QueueRepository;
import com.example.booking.domain.queue.RedisQueue;
import com.example.booking.domain.queue.RedisQueueRepository;
import com.example.booking.domain.queue.RedisToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepositoryImpl  implements RedisQueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public RedisToken addToQueue(RedisToken token, String tokenValue) {
        redisTemplate.opsForZSet().add(RedisQueue.getQueueKey(), tokenValue, token.getTimestamp());
        return token;
    }

    @Override
    public Long getPositionInQueue(String tokenValue) {
        Long rank = redisTemplate.opsForZSet().rank(RedisQueue.getQueueKey(), tokenValue);
        return rank;
    }
}
