package com.example.booking.infra.concert;

import com.example.booking.infra.concert.entity.SeatEntity;
import com.example.booking.infra.concert.entity.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findByScheduleId(long scheduleId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM SeatEntity s WHERE s.id = :seatId")
    Optional<SeatEntity> findByIdForUpdate(@Param("seatId") Long seatId);

    List<SeatEntity> findByScheduleIdAndStatus(long scheduleId, SeatStatus seatStatus);
}
