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

    @Transactional
    public User chargePoint(long userId, int amount) {
        try {

            User user = userRepository.findByIdWithLock(userId);
            user = user.charge(amount);
            return userRepository.save(user);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw e;
        }
    }


    @Transactional
    public User usePoint(long userId, Reservation reservation) {
        Reservation getReservation = reservationRepository.findById(reservation.getId());


        Seat seat = getReservation.getSeat();
        seat.getSeatNumber();

        int totalPrice = getReservation.getTotalPrice();
        User user = userRepository.findByIdWithLock(userId);
        user.use(totalPrice);

        return userRepository.save(user);
    }


    public void restorePoints(User user, int amount) {
        user.restore(amount);
        userRepository.save(user);
    }
}
