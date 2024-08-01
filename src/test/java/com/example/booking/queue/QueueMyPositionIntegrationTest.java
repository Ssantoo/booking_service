package com.example.booking.queue;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QueueMyPositionIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(QueueMyPositionIntegrationTest.class);

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

        for (long i = 2; i <= 31; i++) {
            redisQueueService.generate(i);
        }
    }

    @AfterEach
    public void tearDown() {
        redisTemplate.delete(QUEUE_KEY);
    }

    @Test
    public void 대기열_31번_위치_확인_테스트() {
        Long userId = 1L;
        RedisToken token = redisQueueService.generate(userId);
        logger.info("토큰 : {}", token);
        Long position = redisQueueService.getQueuePosition(token.getTokenValue());

        assertThat(position).isNotNull();
        assertThat(position).isEqualTo(31L);
    }
}
