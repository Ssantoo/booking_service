package com.example.booking.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Captor
    private ArgumentCaptor<User> userCaptor;


    @Test
    void 포인트를_조회_한다() {
        long userId = 1L;
        User mockUser = User.builder()
                .id(userId)
                .name("조현재")
                .point(1000)
                .build();

        given(userRepository.findById(userId)).willReturn(mockUser);

        User result = userService.getUserPoint(userId);

        assertNotNull(result);
        assertEquals(1000, result.getPoint());
    }

    @Test
    void 포인트를_충전_한다() {
        long userId = 1L;
        int chargeAmount = 500;
        User mockUser = User.builder()
                .id(userId)
                .name("조현재")
                .point(1000)
                .build();

        User chargedUser = mockUser.charge(chargeAmount);

        given(userRepository.findById(userId)).willReturn(mockUser);
        given(userRepository.charge(any(User.class))).willReturn(chargedUser);

        User result = userService.chargePoint(userId, chargeAmount);

        verify(userRepository).charge(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertNotNull(result);
        assertEquals(1500, capturedUser.getPoint());
        assertEquals(chargedUser, result);
    }

    @Test
    void 유저_존재_여부_확인한다() {
        long userId = 1L;
        User mockUser = User.builder()
                .id(userId)
                .name("조현재")
                .point(1000)
                .build();

        given(userRepository.findById(userId)).willReturn(mockUser);

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

}
