package com.example.booking.infra.payment;

import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.event.ReservationOutboxRepository;
import com.example.booking.infra.concert.ReservationJpaRepository;
import com.example.booking.infra.concert.entity.SeatEntity;
import com.example.booking.infra.concert.entity.SeatStatusOutboxEntity;
import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import com.example.booking.support.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReservationOutboxRepositoryImpl implements ReservationOutboxRepository {

    private final ReservationOutboxJpaRepository reservationOutboxJpaRepository;


    @Override
    public ReservationOutbox save(ReservationOutbox reservationOutbox) {
        return reservationOutboxJpaRepository.save(ReservationOutboxEntity.from(reservationOutbox)).toModel();
    }

    @Override
    public ReservationOutbox findByReservationId(Long reservationId) {
        return reservationOutboxJpaRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("찾을 수 없습니다.", reservationId)).toModel();
    }

    @Override
    public Optional<ReservationOutbox> findById(Long outboxId) {
        return reservationOutboxJpaRepository.findById(outboxId).map(ReservationOutboxEntity::toModel);
    }

    @Override
    public List<ReservationOutbox> findAllByStatus(ReservationOutboxStatus reservationOutboxStatus) {
        return reservationOutboxJpaRepository.findAllByStatus(reservationOutboxStatus).stream()
                .map(ReservationOutboxEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementReservation(ReservationOutbox outbox) {
        reservationOutboxJpaRepository.save(ReservationOutboxEntity.from(outbox));
    }


}
