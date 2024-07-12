package com.example.booking.infra.payment.entity;

import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentStatus;
import com.example.booking.infra.concert.entity.ReservationEntity;
import com.example.booking.infra.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationEntity reservation;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    public static PaymentEntity from(Payment payment) {
        return PaymentEntity.builder()
                .user(UserEntity.from(payment.getUserId()))
                .reservation(ReservationEntity.from(payment.getReservationId()))
                .amount(payment.getAmount())
                .paymentTime(payment.getPaymentTime())
                .status(payment.getStatus())
                .build();
    }

    public Payment toModel() {
        return new Payment(
            this.user.toModel(),
            this.reservation.toModel(),
            this.amount,
            this.paymentTime,
            this.status
        );
    }
}
