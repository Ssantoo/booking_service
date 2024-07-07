package com.example.booking.controller.concert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/concert")
public class ConcertController {

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

    //예약 가능 날짜
    @GetMapping("/{concertId}/available-dates")
    public ResponseEntity<List<String>> getAvailableDates(@PathVariable String concertId) {
        List<String> dates = Arrays.asList("2024-07-05", "2024-07-06", "2024-07-07");
        return ResponseEntity.ok(dates);
    }

    //예약 가능 좌석
    @GetMapping("/{concertId}/available-seats")
    public ResponseEntity<List<Integer>> getAvailableSeats(@PathVariable String concertId, @RequestParam String date) {
        List<Integer> seats = Arrays.asList(1, 2, 3, 4, 5);
        return ResponseEntity.ok(seats);
    }

}
