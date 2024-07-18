package com.example.booking.domain.queue;

import com.example.booking.infra.token.QueueJpaRepository;
import com.example.booking.infra.token.entity.TokenEntity;
import com.example.booking.infra.token.entity.TokenStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.*;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class QueuePerformanceTest {

    @Mock
    private QueueJpaRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    private List<TokenEntity> generateMockTokens(int count, TokenStatus status) {
        List<TokenEntity> tokens = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TokenEntity token = TokenEntity.builder()
                    .userId((long) i)
                    .token("token_" + i)
                    .expiredAt(LocalDateTime.now().plusDays(1))
                    .status(status)
                    .createdAt(LocalDateTime.now().minusMinutes(i % 10))
                    .build();
            tokens.add(token);
        }
        return tokens;
    }

    @Test
    public void CountByStatus_성능_체크() {
        given(queueRepository.countByStatus(TokenStatus.ACTIVE)).willReturn(10000000);

        long startTime = System.currentTimeMillis();
        int activeTokenCount = queueRepository.countByStatus(TokenStatus.ACTIVE);
        long endTime = System.currentTimeMillis();
        print("countByStatus 시간 체크: " + (endTime - startTime) + " ms");
    }

    @Test
    public void findTopNByStatusOrderByCreatedAt_성능_체크() {
        int limit = 100;
        List<TokenEntity> mockTokens = generateMockTokens(limit, TokenStatus.WAITING);
        given(queueRepository.findTopNByStatusOrderByCreatedAt(eq(TokenStatus.WAITING), anyInt()))
                .willReturn(mockTokens);

        long startTime = System.currentTimeMillis();
        List<TokenEntity> targetTokens = queueRepository.findTopNByStatusOrderByCreatedAt(TokenStatus.WAITING, limit);
        long endTime = System.currentTimeMillis();
        print("FindTopNByStatusOrderByCreatedAt 시간체크: " + (endTime - startTime) + " ms");
    }

    @Test
    public void findActiveWithinLastMinutes_성능_체크 () {
        List<TokenEntity> mockTokens = generateMockTokens(10000000, TokenStatus.ACTIVE);
        given(queueRepository.findActiveWithinLastMinutes()).willReturn(mockTokens);

        long startTime = System.currentTimeMillis();
        List<TokenEntity> activeTokens = queueRepository.findActiveWithinLastMinutes();
        long endTime = System.currentTimeMillis();
        print("findActiveWithinLastMinutes 시간체크: " + (endTime - startTime) + " ms");
    }





}
