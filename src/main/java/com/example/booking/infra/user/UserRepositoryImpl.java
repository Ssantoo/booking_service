package com.example.booking.infra.user;

import com.example.booking.common.exception.ResourceNotFoundException;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.infra.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @Override
    public User findById(long userId) {
        return userJpaRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("유저가 존재하지 않습니다", userId))
                .toModel();
    }

    @Override
    public User charge(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public User findByIdWithLock(long userId) {
        return userJpaRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다", userId))
                .toModel();
    }
}
