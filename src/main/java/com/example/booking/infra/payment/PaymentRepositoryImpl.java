package com.example.booking.infra.payment;

import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentRepository;
import com.example.booking.infra.token.TokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.example.booking.infra.payment.entity.PaymentEntity;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;


    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(PaymentEntity.from(payment)).toModel();
    }
}
