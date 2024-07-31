package com.example.booking.domain.concert;


import com.example.booking.domain.queue.QueueService;
import com.example.booking.domain.queue.Token;


import com.example.booking.infra.concert.entity.SeatStatus;
import com.example.booking.support.config.RedisConfig;
import com.example.booking.support.exception.AlreadyOccupiedException;
import com.example.booking.support.exception.NotReservableException;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    private final ScheduleRepository scheduleRepository;

    private final SeatRepository seatRepository;

    private final ReservationRepository reservationRepository;

    private final QueueService queueService;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ConcertService.class);


    public List<Concert> getConcertList() {

        List<Concert> concertList = (List<Concert>) redisTemplate.opsForValue().get(RedisConfig.listKey());

        if (concertList == null) {
            logger.info("데이터베이스에서 조회");
            concertList = concertRepository.findAll();
            redisTemplate.opsForValue().set(RedisConfig.listKey(), concertList, 10, TimeUnit.MINUTES);
        }

        return concertList;
    }

    public List<Schedule> getDates(long concertId) {
        return scheduleRepository.findByConcertId(concertId);
    }


    public List<Seat> getAvailableSeats(long scheduleId) {
//        List<Seat> allSeats = seatRepository.findByScheduleId(scheduleId);
//        return allSeats.stream().filter(Seat::isAvailable).collect(Collectors.toList());
        return seatRepository.findAvailableSeatsByScheduleId(scheduleId, SeatStatus.AVAILABLE);
    }

    @Transactional
    public Reservation reserve(Reservation reservation, String token) {
        // 토큰 유효성 검사
//        Token tokens = queueService.findByToken(token)
//                .orElseThrow(() -> new NotReservableException("유효하지 않은 토큰입니다."));
//        tokens.validateActive();

        // 좌석을 비관적 락으로 설정
        Seat seat = seatRepository.findByIdForUpdate(reservation.getSeat().getId())
                .orElseThrow(() -> new IllegalStateException("해당 좌석을 찾을 수 없습니다."));

        // 좌석 상태 확인 및 예약 처리
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new AlreadyOccupiedException("이미 예약된 좌석입니다.");
        }

        seat.hold(); // 좌석 상태를 '예약 중'으로 변경
        seatRepository.save(seat); // 좌석 상태 저장

        // 예약 상태를 PENDING으로 설정하고 저장
        reservation.hold();
        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }


}
