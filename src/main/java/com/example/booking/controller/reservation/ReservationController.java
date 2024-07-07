package com.example.booking.controller.reservation;

import com.example.booking.controller.reservation.dto.ReservationRequest;
import com.example.booking.controller.reservation.dto.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    /**좌석 예약 요청
     - 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
     - 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
     - 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 다른 사용자는 예약할 수 없어야 한다.
     */
    @PostMapping("/reserve-seat")
    public ResponseEntity<ReservationResponse> reserveSeat(@RequestBody ReservationRequest reservationRequest) {
        ReservationResponse response = new ReservationResponse("reserved", reservationRequest.getSeatNumber(), 5);
        return ResponseEntity.ok(response);
    }

}
