package com.example.booking.domain.queue;

import com.example.booking.controller.queue.scheduler.TokenExpiryScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenExpirySchedulerTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenExpiryScheduler tokenExpiryScheduler;

    @Test
    public void 스케줄러_토큰_만료_테스트() {
        tokenExpiryScheduler.expireTokens();

        verify(tokenService, times(1)).expireTokens();
        verify(tokenService, times(1)).expireInactiveActiveTokens();
    }
}
