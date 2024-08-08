package com.example.booking.infra.concert;

import com.example.booking.domain.concert.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationMockApiClient {

    public void sendData(Reservation reservation) {
        try {
            log.info("sendData 예약정보 : {}", reservation);
        } catch (Exception e) {
            log.error("sendData 예약 데이터 저장 실패 => " + e.getMessage());
        }
    }
}
