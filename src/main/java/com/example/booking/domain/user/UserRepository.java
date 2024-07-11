package com.example.booking.domain.user;

public interface UserRepository {
    User findById(long userId);

    User charge(User user);

    User findByIdWithLock(long userId);
}
