package com.example.booking.controller.queue.scheduler;

import com.example.booking.domain.queue.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenExpiryScheduler {

    private final TokenService tokenService;

    @Scheduled(fixedRate = 300000)
    public void scheduledExpireTokens() {
        expireTokens();
        activateQueuedUsers();
    }

    //비활성화 스케줄러
    public void expireTokens() {
        tokenService.expireTokens();
        tokenService.expireInactiveActiveTokens();
    }

    //active 스케줄러
    public void activateQueuedUsers() {
        tokenService.activateQueuedUsers();
    }
}
