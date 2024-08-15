package com.example.booking.infra.payment;

import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationOutboxJpaRepository extends JpaRepository<ReservationOutboxEntity, Long> {

    Optional<ReservationOutboxEntity> findAllByStatus(ReservationOutboxStatus reservationOutboxStatus);
}
