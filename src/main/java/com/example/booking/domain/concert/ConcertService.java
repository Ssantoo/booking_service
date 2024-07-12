package com.example.booking.domain.concert;

import com.example.booking.common.exception.AlreadyOccupiedException;

import com.example.booking.common.exception.NotReservableException;
import com.example.booking.domain.queue.Token;
import com.example.booking.domain.queue.TokenService;
import com.example.booking.infra.concert.entity.ReservationStatus;

import com.example.booking.infra.concert.entity.SeatStatus;
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

    private final TokenService tokenService;
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
    public Reservation reserve(Reservation reservation, String token) {

        // 토큰의 활성 상태 확인
        Token tokens = tokenService.findByToken(token)
                .orElseThrow(() -> new NotReservableException("유효하지 않은 토큰입니다."));
        tokens.validateActive();

//        Seat seat = seatRepository.findById(reservation.getSeatId())
//                .orElseThrow(() -> new IllegalStateException("해당 좌석을 찾을 수 없습니다."));

        //좌석을 비관적 락 설정
        Seat seat = seatRepository.findByIdForUpdate(reservation.getSeatId())
                .orElseThrow(() -> new IllegalStateException("해당 좌석을 찾을 수 없습니다."));

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new AlreadyOccupiedException("이미 예약된 좌석입니다.");
        }

        seat.hold();
        seatRepository.save(seat);

        //예약에 좌석이 있는지 체크
//        Optional<Reservation> existingReservation = reservationRepository.findByConcertScheduleIdAndSeatIdForUpdate(
//                reservation.getConcertScheduleId(), reservation.getSeatId());
//
//        if (existingReservation.isPresent() && existingReservation.get().getStatus() == ReservationStatus.PENDING) {
//            throw new AlreadyOccupiedException("이미 예약된 좌석입니다.");
//        }

        // 기존 예약이 없으면 예약 상태를 PENDING으로 변경하고 좌석 상태도 변경 및 상태 저장
        reservation.hold();

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }


}
