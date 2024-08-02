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

import java.util.Objects;
import java.util.Set;

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
    private static final String ACTIVE_KEY_PREFIX = "ACTIVE:";
    private static final int MAX_ACTIVE_USERS = 10;

    @BeforeEach
    public void setUp() {
        redisTemplate.delete(QUEUE_KEY);

        Set<String> activeKeys = redisTemplate.keys(ACTIVE_KEY_PREFIX + "*");
        if (activeKeys != null && !activeKeys.isEmpty()) {
            redisTemplate.delete(activeKeys);
        }

        User user = User.builder()
                .id(1L)
                .name("조현재")
                .point(1000)
                .build();
        userRepository.save(user);

        for (long i = 2; i <= 21; i++) {
            redisQueueService.generate(i);
        }
    }

    @AfterEach
    public void tearDown() {
//        redisTemplate.delete(QUEUE_KEY);
//
////        Set<String> activeKeys = redisTemplate.keys(ACTIVE_KEY_PREFIX + "*");
////        if (activeKeys != null && !activeKeys.isEmpty()) {
////            redisTemplate.delete(activeKeys);
////        }
    }

    @Test
    public void 대기열_21번_위치_확인_테스트() {
        Long userId = 1L;
        RedisToken token = redisQueueService.generate(userId);
        logger.info("토큰 : {}", token);
        Long position = redisQueueService.getQueuePosition(token.getTokenValue());

        assertThat(position).isNotNull();
        assertThat(position).isEqualTo(21L);
    }

    @Test
    public void 활성화된_토큰_인원수_확인_테스트() {
        redisQueueService.activateUsers();

        int activeUserCount = redisQueueService.getActiveUserCount();
        logger.info("활성화된 사용자 수: {}", activeUserCount);

        assertThat(activeUserCount).isGreaterThan(0);
        assertThat(activeUserCount).isLessThanOrEqualTo(MAX_ACTIVE_USERS);
    }

    @Test
    public void 활성화된_인원_대기열_인원체크() {

        redisQueueService.activateUsers();

        int activeUserCount = redisQueueService.getActiveUserCount();
        logger.info("활성화된 사용자 수: {}", activeUserCount);

        assertThat(activeUserCount).isGreaterThan(0);
        assertThat(activeUserCount).isLessThanOrEqualTo(MAX_ACTIVE_USERS);

        int waitUserCount = Objects.requireNonNull(redisTemplate.opsForZSet().size(QUEUE_KEY)).intValue();
        logger.info("대기 중인 사용자 수: {}", waitUserCount);

        Set<String> waitingUsers = redisTemplate.opsForZSet().range(QUEUE_KEY, 0, -1);
        logger.info("대기 중인 사용자 목록: {}", waitingUsers);

        assertThat(waitUserCount).isEqualTo(10);
    }

    @Test
    public void 만료된_토큰_처리_테스트() throws InterruptedException {
        // 토큰 생성 및 활성화
        for (long i = 1; i <= 15; i++) {
            redisQueueService.generate(i);
        }

        // 6분 후 토큰 만료
        Thread.sleep(360000);

        // 만료된 토큰 처리
        redisQueueService.handleExpiredTokens();

        // 활성화된 사용자 수 확인
        int activeUserCount = redisQueueService.getActiveUserCount();
        logger.info("활성화된 사용자 수: {}", activeUserCount);

        // 대기 중인 사용자 수 확인
        int waitUserCount = Objects.requireNonNull(redisTemplate.opsForZSet().size(QUEUE_KEY)).intValue();
        logger.info("대기 중인 사용자 수: {}", waitUserCount);

        assertThat(activeUserCount).isEqualTo(0);
        assertThat(waitUserCount).isEqualTo(15);
    }


}
