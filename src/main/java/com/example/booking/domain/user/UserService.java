package com.example.booking.domain.user;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.ReservationRepository;
import com.example.booking.support.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public User getUserPoint(long userId) {
        return userRepository.findById(userId);
    }

    public User getUserById(Long userId) {
    return userRepository.findById(userId);
}

//    @Transactional
//    public User chargePoint(long userId, int amount) {
//        System.out.println("포인트 충전 : " + userId);
//
//        User user = userRepository.findByIdWithLock(userId);
//        user = user.charge(amount);
//        System.out.println("충전: " + userId);
//        return userRepository.charge(user);
//    }
    @Transactional
    public User chargePoint(long userId, int amount) {
        try {

            User user = userRepository.findByIdWithLock(userId);
            user = user.charge(amount);
            System.out.println("충전: " + userId);
            return userRepository.save(user);
        } catch (ObjectOptimisticLockingFailureException e) {
            // 충돌 처리 로직 (예: 재시도 로직)
            System.out.println("Optimistic Locking 충돌 발생: " + e.getMessage());
            throw e;
        }
    }


    @Transactional
    public User usePoint(long userId, Reservation reservation) {
        Reservation getReservation = reservationRepository.findById(reservation.getId());
        User user = userRepository.findByIdWithLock(userId);
        User updatedUser = user.use(getReservation.getTotalPrice());
        return userRepository.save(updatedUser);
    }



}
