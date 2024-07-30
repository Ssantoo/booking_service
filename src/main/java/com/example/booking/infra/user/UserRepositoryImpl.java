package com.example.booking.infra.user;

import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.infra.user.entity.UserEntity;
import com.example.booking.support.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @Override
    public User findById(long userId) {
//        System.out.println("userid" + userId);
        return userJpaRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("유저가 존재하지 않습니다", userId))
                .toModel();
    }

    @Override
    public User charge(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public User findByIdWithLock(long userId) {
//        System.out.println("userid" + userId);
        return userJpaRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다", userId))
                .toModel();
    }

    @Override
    public User save(User updatedUser) {
        return userJpaRepository.save(UserEntity.from(updatedUser)).toModel();
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(UserEntity::toModel).collect(Collectors.toList());

    }
}
