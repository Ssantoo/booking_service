package com.example.booking.domain.user;

import java.util.List;

public interface UserRepository {
    User findById(long userId);

    User charge(User user);

    User findByIdWithLock(long userId);

    User save(User updatedUser);

    List<User> findAll();
}
