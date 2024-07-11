package com.example.booking.domain.concert;

import com.example.booking.common.exception.AlreadyOccupiedException;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.domain.user.UserService;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ConcertService concertService;

    @Test
    public void 콘서트_목록_조회() {
        List<Concert> mockConcertList = List.of(
                new Concert(1L, "해리포터 1", "마법사의돌"),
                new Concert(2L, "해리포터 2", "신비한")
        );

        given(concertRepository.findAll()).willReturn(mockConcertList);

        List<Concert> result = concertService.getConcertList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("해리포터 1", result.get(0).getName());
    }

    @Test
    public void 예약_가능_날짜_조회() {
        long concertId = 1L;
        List<Schedule> mockScheduleList = List.of(
                new Schedule(1L, LocalDateTime.of(2024, 7, 5, 0, 0), 100, 50, new Concert(concertId, "해리포터 1", "마법사의돌")),
                new Schedule(2L, LocalDateTime.of(2024, 7, 6, 0, 0), 100, 50, new Concert(concertId, "해리포터 1", "마법사의돌")),
                new Schedule(3L, LocalDateTime.of(2024, 7, 7, 0, 0), 100, 50, new Concert(concertId, "해리포터 1", "마법사의돌"))
        );

        given(scheduleRepository.findByConcertId(concertId)).willReturn(mockScheduleList);

        List<Schedule> result = concertService.getDates(concertId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(LocalDateTime.of(2024, 7, 5, 0, 0), result.get(0).getDateTime());
    }

    @Test
    public void 예약_좌석_조회() {
        long scheduleId = 1L;

        Schedule mockSchedule = Schedule.builder()
                .id(scheduleId)
                .dateTime(LocalDateTime.of(2024, 7, 5, 0, 0))
                .totalSeats(100)
                .availableSeats(50)
                .concert(new Concert(1L, "해리포터 1", "마법사의돌"))
                .build();


        List<Seat> mockSeatList = List.of(
                Seat.builder().id(1L).seatNumber(1).price(100).status(SeatStatus.AVAILABLE).schedule(mockSchedule).build(),
                Seat.builder().id(2L).seatNumber(2).price(100).status(SeatStatus.AVAILABLE).schedule(mockSchedule).build()
        );

        given(seatRepository.findByScheduleId(scheduleId)).willReturn(mockSeatList);

        List<Seat> result = concertService.getSeats(scheduleId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getSeatNumber());
    }

    @Test
    public void 예약_성공() {
        Reservation mockReservation = Reservation.builder()
                .id(1L)
                .userId(1L)
                .concertScheduleId(1L)
                .seatId(1L)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(100)
                .build();

        given(reservationRepository.findByConcertScheduleIdAndSeatIdForUpdate(1L, 1L)).willReturn(Optional.empty());
        given(reservationRepository.save(mockReservation)).willReturn(mockReservation);

        Reservation result = concertService.reserve(mockReservation);

        assertNotNull(result);
        assertEquals(ReservationStatus.PENDING, result.getStatus());
    }

    @Test
    public void 예약_실패_이미_예약된_좌석() {
        Reservation mockReservation = Reservation.builder()
                .id(1L)
                .userId(1L)
                .concertScheduleId(1L)
                .seatId(1L)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(100)
                .build();

        given(reservationRepository.findByConcertScheduleIdAndSeatIdForUpdate(1L, 1L)).willReturn(Optional.of(mockReservation));

        assertThrows(AlreadyOccupiedException.class, () -> {
            concertService.reserve(mockReservation);
        });
    }

    @Test
    public void 예약_조회() {
        long reservationId = 1L;
        Reservation mockReservation = Reservation.builder()
                .id(reservationId)
                .userId(1L)
                .concertScheduleId(1L)
                .seatId(1L)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(100)
                .build();

        given(reservationRepository.findById(reservationId)).willReturn(mockReservation);

        Reservation result = concertService.getReservationById(reservationId);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());
    }

}
