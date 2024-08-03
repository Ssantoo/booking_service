package com.example.booking.queue;

import com.example.booking.domain.concert.ConcertServiceTest;
import com.example.booking.domain.queue.RedisQueueService;
import com.example.booking.domain.queue.RedisToken;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QueueGenerateIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(QueueGenerateIntegrationTest.class);

    @Autowired
    private RedisQueueService redisQueueService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

     @Autowired
     private UserRepository userRepository;

    private static final String QUEUE_KEY = "WAIT";

    @BeforeEach
    public void setUp() {
        redisTemplate.delete(QUEUE_KEY);

        User user = User.builder()
                .id(1L)
                .name("조현재")
                .point(1000)
                .build();
         userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        redisTemplate.delete(QUEUE_KEY);
    }

    @Test
    public void 토큰_발급_통합_테스트() {
        Long userId = 1L;
        redisQueueService.generate(userId);

        Set<String> tokens = redisTemplate.opsForZSet().range("WAIT", 0, -1);
        logger.info("레디스토큰 : {}" , tokens);

        assertThat(tokens).isNotNull();
        assertThat(tokens).hasSize(1);

        String expectedTokenSuffix = ":" + userId;
        logger.info("토큰저장 : {}" , expectedTokenSuffix);
        boolean tokenExists = tokens.stream().anyMatch(token -> token.endsWith(expectedTokenSuffix));

        assertThat(tokenExists).isTrue();
    }
}
