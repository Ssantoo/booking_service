package com.example.booking.domain.token;

import com.example.booking.domain.queue.Token;
import com.example.booking.domain.queue.TokenRepository;
import com.example.booking.domain.queue.TokenService;
import com.example.booking.infra.token.entity.TokenStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Captor
    private ArgumentCaptor<Token> tokenCaptor;

    private Token activeToken;
    private Token expiredToken;
    private Token inactiveToken;

    @BeforeEach
    public void setUp() {
        activeToken = Token.builder()
                .id(1L)
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusHours(1))
                .status(TokenStatus.ACTIVE)
                .lastActivityAt(LocalDateTime.now().minusMinutes(15))
                .build();

        expiredToken = Token.builder()
                .id(2L)
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().minusHours(1))
                .status(TokenStatus.ACTIVE)
                .lastActivityAt(LocalDateTime.now().minusHours(2))
                .build();

        inactiveToken = Token.builder()
                .id(3L)
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusHours(1))
                .status(TokenStatus.ACTIVE)
                .lastActivityAt(LocalDateTime.now().minusMinutes(20))
                .build();
    }

    @Test
    public void 토큰_생성_테스트() {
        given(tokenRepository.save(any(Token.class))).willReturn(activeToken);

        Token createdToken = tokenService.generate(1L);

        assertNotNull(createdToken);
        assertEquals(activeToken.getUserId(), createdToken.getUserId());
        assertEquals(activeToken.getStatus(), createdToken.getStatus());
        assertEquals(activeToken.getToken(), createdToken.getToken());

        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void 만료된_토큰_테스트() {
        given(tokenRepository.findActiveTokens()).willReturn(List.of(activeToken, expiredToken));

        tokenService.expireTokens();

        verify(tokenRepository, times(1)).findActiveTokens();
        verify(tokenRepository, times(1)).save(tokenCaptor.capture());

        Token savedToken = tokenCaptor.getValue();
        assertEquals(TokenStatus.EXPIRED, savedToken.getStatus());
        assertTrue(savedToken.isExpired());
    }

    @Test
    public void 비활성화된_토큰_테스트() {
        given(tokenRepository.findActiveTokens()).willReturn(List.of(activeToken, inactiveToken));

        tokenService.expireInactiveActiveTokens();

        verify(tokenRepository, times(1)).findActiveTokens();
        verify(tokenRepository, times(2)).save(tokenCaptor.capture());

        List<Token> savedTokens = tokenCaptor.getAllValues();
        assertEquals(2, savedTokens.size());

        savedTokens.forEach(savedToken -> {
            assertEquals(TokenStatus.EXPIRED, savedToken.getStatus());
            assertTrue(savedToken.isInactiveForDuration(Duration.ofMinutes(10)));
        });
    }


}
