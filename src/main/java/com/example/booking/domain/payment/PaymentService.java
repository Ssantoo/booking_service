package com.example.booking.domain.payment;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment pay(User user, Reservation reservation) {
        final User updatedUser = user.use(reservation.getTotalPrice());
        final Payment payment = Payment.pay(updatedUser, reservation);
        return paymentRepository.save(payment);
    }
}
