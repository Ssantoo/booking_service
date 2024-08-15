package com.example.booking.infra.payment;

import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationOutboxJpaRepository extends JpaRepository<ReservationOutboxEntity, Long> {

}
