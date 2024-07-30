package com.example.booking.domain.concert;

import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> findByConcertScheduleIdAndSeatIdForUpdate(Long concertScheduleId, Long seatId);

    Reservation save(Reservation reservation);

    Reservation findById(Long reservationId);

    Reservation findByIdWithLock(Reservation reservation);
}
