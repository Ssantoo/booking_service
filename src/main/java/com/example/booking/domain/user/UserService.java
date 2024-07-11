package com.example.booking.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserPoint(long userId) {
        return userRepository.findById(userId);
    }
    @Transactional
    public User chargePoint(long userId, int amount) {
        final User user = userRepository.findByIdWithLock(userId).charge(amount);
        final User updatedUserPoint = userRepository.charge(user);
        return updatedUserPoint;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
