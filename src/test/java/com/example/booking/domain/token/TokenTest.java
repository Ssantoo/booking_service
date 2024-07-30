//package com.example.booking.domain.token;
//
//import com.example.booking.common.exception.NotReservableException;
//import com.example.booking.domain.queue.Token;
//import com.example.booking.infra.token.entity.TokenStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//
//public class TokenTest {
//
//    private Token activeToken;
//    private Token expiredToken;
//    private Token inactiveToken;
//
//    @BeforeEach
//    public void setUp() {
//        activeToken = Token.builder()
//                .id(1L)
//                .userId(1L)
//                .token(UUID.randomUUID().toString())
//                .expiredAt(LocalDateTime.now().plusHours(1))
//                .status(TokenStatus.ACTIVE)
//                .lastActivityAt(LocalDateTime.now())
//                .build();
//
//        expiredToken = Token.builder()
//                .id(2L)
//                .userId(1L)
//                .token(UUID.randomUUID().toString())
//                .expiredAt(LocalDateTime.now().minusHours(1))
//                .status(TokenStatus.ACTIVE)
//                .lastActivityAt(LocalDateTime.now().minusHours(2))
//                .build();
//
//        inactiveToken = Token.builder()
//                .id(3L)
//                .userId(1L)
//                .token(UUID.randomUUID().toString())
//                .expiredAt(LocalDateTime.now().plusHours(1))
//                .status(TokenStatus.ACTIVE)
//                .lastActivityAt(LocalDateTime.now().minusMinutes(20))
//                .build();
//    }
//
//    @Test
//    public void 토큰_생성_테스트() {
//        Long userId = 4L;
//        Token newToken = Token.generate(userId);
//
//        assertNotNull(newToken);
//        assertEquals(userId, newToken.getUserId());
//        assertEquals(TokenStatus.WAITING, newToken.getStatus());
//        assertTrue(newToken.getExpiredAt().isAfter(LocalDateTime.now()));
//        assertTrue(newToken.getLastActivityAt().isBefore(newToken.getExpiredAt()));
//    }
//
//    @Test
//    public void 토큰_만료_테스트() {
//        Token expired = activeToken.expire();
//
//        assertNotNull(expired);
//        assertEquals(activeToken.getId(), expired.getId());
//        assertEquals(TokenStatus.EXPIRED, expired.getStatus());
//        assertEquals(activeToken.getUserId(), expired.getUserId());
//        assertEquals(activeToken.getToken(), expired.getToken());
//    }
//
//    @Test
//    public void 토큰_활성화_테스트() {
//        Token activated = inactiveToken.activate();
//
//        assertNotNull(activated);
//        assertEquals(inactiveToken.getId(), activated.getId());
//        assertEquals(TokenStatus.ACTIVE, activated.getStatus());
//        assertEquals(inactiveToken.getUserId(), activated.getUserId());
//        assertEquals(inactiveToken.getToken(), activated.getToken());
//        assertTrue(activated.getLastActivityAt().isAfter(inactiveToken.getLastActivityAt()));
//    }
//
//    @Test
//    public void 마지막_활동_시간_업데이트_테스트() {
//        LocalDateTime beforeUpdate = activeToken.getLastActivityAt();
//        Token updated = activeToken.updateLastActivity();
//
//        assertNotNull(updated);
//        assertEquals(activeToken.getId(), updated.getId());
//        assertEquals(activeToken.getUserId(), updated.getUserId());
//        assertEquals(activeToken.getToken(), updated.getToken());
//        assertTrue(updated.getLastActivityAt().isAfter(beforeUpdate));
//    }
//
//    @Test
//    public void 토큰_유효기간_만료_체크_테스트() {
//        assertTrue(expiredToken.isExpired());
//        assertFalse(activeToken.isExpired());
//    }
//
//    @Test
//    public void 토큰_비활성화_기간_체크_테스트() {
//        assertTrue(inactiveToken.isInactiveForDuration(Duration.ofMinutes(10)));
//        assertFalse(activeToken.isInactiveForDuration(Duration.ofMinutes(10)));
//    }
//
//    @Test
//    public void 토큰_활성_상태_검증_테스트() {
//        assertDoesNotThrow(() -> activeToken.validateActive());
//        assertThrows(NotReservableException.class, () -> expiredToken.validateActive());
//    }
//}
