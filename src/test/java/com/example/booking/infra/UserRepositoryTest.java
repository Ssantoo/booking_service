package com.example.booking.infra;

import com.example.booking.domain.user.User;
import com.example.booking.infra.user.UserJpaRepository;
import com.example.booking.infra.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void 유저를_조회한다() {
        Long userId = 1L;

        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));

        User user = userEntity.toModel();

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("조현재", user.getName());
        assertEquals(1000, user.getPoint());
    }

    @Test
    void 유저를_비관적_잠금으로_조회한다() {
        Long userId = 1L;

        UserEntity userEntity = userJpaRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));

        User user = userEntity.toModel();

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("조현재", user.getName());
        assertEquals(1000, user.getPoint());
    }

    @Test
    void 유저_포인트를_충전한다() {
        Long userId = 1L;
        int chargeAmount = 500;

        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));

        User user = userEntity.toModel().charge(chargeAmount);

        UserEntity updatedUserEntity = userJpaRepository.save(UserEntity.from(user));
        User updatedUser = updatedUserEntity.toModel();

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
        assertEquals("조현재", updatedUser.getName());
        assertEquals(1500, updatedUser.getPoint());
    }
}
