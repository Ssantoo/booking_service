package com.example.booking.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    public void 포인트_충전_성공() {
        User user = User.builder()
                .id(1L)
                .name("조현재")
                .point(1000)
                .build();

        User chargedUser = user.charge(500);

        assertEquals(1500, chargedUser.getPoint());
    }

    @Test
    public void 포인트_사용_성공() {
        User user = User.builder()
                .id(1L)
                .name("조현재")
                .point(1000)
                .build();

        User usedUser = user.use(500);

        assertEquals(500, usedUser.getPoint());
    }

    @Test
    public void 포인트_음수_충전_실패() {
        User user = User.builder()
                .id(1L)
                .name("조현재")
                .point(1000)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            user.charge(-500);
        });
    }

    @Test
    public void 포인트_부족_사용_실패() {
        User user = User.builder()
                .id(1L)
                .name("조현재")
                .point(1000)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            user.use(1500);
        });
    }


}
