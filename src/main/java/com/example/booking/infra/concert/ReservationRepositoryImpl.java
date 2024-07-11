package com.example.booking.infra.concert;

import com.example.booking.common.exception.AlreadyOccupiedException;
import com.example.booking.common.exception.ResourceNotFoundException;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.ReservationRepository;
import com.example.booking.infra.concert.entity.ReservationEntity;
import com.example.booking.infra.concert.entity.ReservationStatus;
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
}
