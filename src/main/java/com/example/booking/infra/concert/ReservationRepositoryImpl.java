package com.example.booking.infra.concert;


import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.ReservationRepository;
import com.example.booking.infra.concert.entity.ReservationEntity;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.support.exception.ResourceNotFoundException;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<Reservation> findByConcertScheduleIdAndSeatIdForUpdate(Long concertScheduleId, Long seatId) {
        return reservationJpaRepository.findByConcertScheduleIdAndSeatIdForUpdate(concertScheduleId, seatId)
                .map(ReservationEntity::toModel);
    }

    public boolean isSeatReserved(Long concertScheduleId, Long seatId) {
        Optional<ReservationEntity> existingReservation = reservationJpaRepository.findByConcertScheduleIdAndSeatIdForUpdate(concertScheduleId, seatId);
        return existingReservation.isPresent() && existingReservation.get().getStatus() == ReservationStatus.PENDING;
    }

    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(ReservationEntity.from(reservation)).toModel();
    }

    @Override
    public Reservation findById(Long reservationId) {
        return reservationJpaRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("예약건이 없습니다.", reservationId)).toModel();
    }

    @Override
    public Reservation findByIdWithLock(Reservation reservation) {
        return reservationJpaRepository.findByIdWithLock(reservation.getId())
                .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다", reservation.getId()))
                .toModel();
    }
}
