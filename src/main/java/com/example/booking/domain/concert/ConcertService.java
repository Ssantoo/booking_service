package com.example.booking.domain.concert;

import com.example.booking.common.exception.AlreadyOccupiedException;

import com.example.booking.infra.concert.entity.ReservationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    private final ScheduleRepository scheduleRepository;

    private final SeatRepository seatRepository;

    private final ReservationRepository reservationRepository;

    public List<Concert> getConcertList() {
        return concertRepository.findAll();
    }

    public List<Schedule> getDates(long concertId) {
        return scheduleRepository.findByConcertId(concertId);
    }


    public List<Seat> getSeats(long scheduleId) {
        return seatRepository.findByScheduleId(scheduleId);
    }

    @Transactional
    public Reservation reserve(Reservation reservation) {

        Optional<Reservation> existingReservation = reservationRepository.findByConcertScheduleIdAndSeatIdForUpdate(
                reservation.getConcertScheduleId(), reservation.getSeatId());

        if (existingReservation.isPresent() && existingReservation.get().getStatus() == ReservationStatus.PENDING) {
            throw new AlreadyOccupiedException("이미 예약된 좌석입니다.");
        }

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }
}
