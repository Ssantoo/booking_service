package com.example.booking.controller.concert;

import com.example.booking.controller.concert.dto.ConcertResponse;
import com.example.booking.controller.concert.dto.ScheduleResponse;
import com.example.booking.controller.concert.dto.SeatResponse;
import com.example.booking.controller.concert.dto.ReservationRequest;
import com.example.booking.controller.concert.dto.ReservationResponse;
import com.example.booking.domain.concert.ConcertService;
import com.example.booking.domain.concert.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "콘서트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;

    /**
     * **예약 가능 날짜 / 좌석 API **
     *
     * - 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
     * - 예약 가능한 날짜 목록을 조회할 수 있습니다.
     *
     * - 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.
     *
     * > 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.
     * >
     */
    //콘서트 목록 조회
    @Operation(summary = "콘서트 목록 조회")
    @GetMapping
    public ResponseEntity<List<ConcertResponse>> getConcertList() {
        List<ConcertResponse> concerts = ConcertResponse.from(concertService.getConcertList());
        return ResponseEntity.ok(concerts);
    }

    //예약 가능 날짜
    @Operation(summary = "콘서트 날짜 조회")
    @GetMapping("/{concertId}/available-dates")
    public ResponseEntity<List<ScheduleResponse>> getAvailableDates(@PathVariable long concertId) {
        List<ScheduleResponse> dates = ScheduleResponse.from(concertService.getDates(concertId));
        return ResponseEntity.ok(dates);
    }

    //좌석 조회
    @Operation(summary = "콘서트 좌석 조회")
    @GetMapping("/{scheduleId}/available-seats")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable long scheduleId) {
        List<SeatResponse> seats = SeatResponse.from(concertService.getSeats(scheduleId));
        return ResponseEntity.ok(seats);
    }


    /**좌석 예약 요청
     - 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
     - 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
     - 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 다른 사용자는 예약할 수 없어야 한다.
     */
    @Operation(summary = "콘서트 좌석 좌석 예약 요청")
    @PostMapping("/reserve-seat")
    public ResponseEntity<ReservationResponse> reserveSeat(@RequestBody ReservationRequest reservationRequest) {
        ReservationResponse response = ReservationResponse.from(concertService.reserve(reservationRequest.toDomain()));
        return ResponseEntity.ok(response);
    }

}
