package com.example.booking.domain.user;

import com.example.booking.domain.concert.Concert;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.ReservationRepository;
import com.example.booking.domain.concert.Seat;
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

        // 트랜잭션 내에서 지연 로딩된 데이터를 미리 초기화
        Seat seat = getReservation.getSeat();
        seat.getSeatNumber();  // 지연 로딩된 필드에 접근하여 초기화

        // 필요한 데이터를 가져와서 처리
        int totalPrice = getReservation.getTotalPrice();
        User user = userRepository.findByIdWithLock(userId);
        user.use(totalPrice);

        return userRepository.save(user);
    }



}
