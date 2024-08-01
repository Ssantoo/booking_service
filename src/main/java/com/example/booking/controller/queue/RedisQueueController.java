package com.example.booking.controller.queue;

import com.example.booking.domain.queue.RedisQueueService;
import com.example.booking.domain.queue.RedisToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis/queue")
public class RedisQueueController {

    private final RedisQueueService redisQueueService;

    /** 토큰
     * 유저 대기열 토큰 기능
     * Redis ZADD 이용하여 추가
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateToken(@RequestParam Long userId) {
        RedisToken token = redisQueueService.generate(userId);
        return ResponseEntity.ok(token.getTokenValue());
    }

    /**
     * 자기 순번 api
     */
    @GetMapping("/position")
    public ResponseEntity<Long> getQueuePosition(@RequestParam String tokenValue) {
        Long position = redisQueueService.getQueuePosition(tokenValue);
        if (position == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(position);
    }

}
