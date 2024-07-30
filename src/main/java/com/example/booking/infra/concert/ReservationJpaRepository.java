package com.example.booking.infra.concert;

import com.example.booking.infra.concert.entity.ReservationEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationEntity r WHERE r.concertScheduleId = :concertScheduleId AND r.seat.id = :seatId")
    Optional<ReservationEntity> findByConcertScheduleIdAndSeatIdForUpdate(@Param("concertScheduleId") Long concertScheduleId, @Param("seatId") Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r from ReservationEntity r where r.id = :id")
    Optional<ReservationEntity> findByIdWithLock(Long id);
}
