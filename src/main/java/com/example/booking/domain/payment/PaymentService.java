package com.example.booking.domain.payment;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.event.PointRestoreEvent;
import com.example.booking.domain.event.SeatStatusChangeEvent;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Payment pay(User user, Reservation reservation) {
        try {
        final User updatedUser = user.use(reservation.getTotalPrice());
        userRepository.save(updatedUser);
        final Payment payment = Payment.pay(updatedUser, reservation);
        paymentRepository.save(payment);

        // 좌석 상태 변경 이벤트 발행
        eventPublisher.publishEvent(new SeatStatusChangeEvent(this, reservation, null));

        return payment;
        } catch (Exception e) {
            // 포인트 복구 이벤트 발행
            eventPublisher.publishEvent(new PointRestoreEvent(this, user, reservation.getTotalPrice()));
            throw e;
        }
    }
}
