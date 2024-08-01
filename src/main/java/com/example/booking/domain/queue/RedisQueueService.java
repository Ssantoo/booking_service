package com.example.booking.domain.queue;

import com.example.booking.infra.token.entity.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisQueueService {

    private final RedisQueueRepository redisQueueRepository;

    public RedisToken generate(Long userId) {
        RedisToken token = RedisToken.createToken(userId);
        String tokenValue = token.getTokenValue();
        redisQueueRepository.addToQueue(token, tokenValue);
        return token;
    }

    public Long getQueuePosition(String tokenValue) {
        Long position = redisQueueRepository.getPositionInQueue(tokenValue);
        return position == null ? null : position + 1;  //zindex가 0부터 시작하니까
    }
}
