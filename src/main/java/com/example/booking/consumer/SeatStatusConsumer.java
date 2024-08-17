package com.example.booking.consumer;

import com.example.booking.domain.concert.ConcertService;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.event.OutboxService;
import com.example.booking.domain.event.ReservationEvent;
import com.example.booking.domain.event.SeatStatusChangeEvent;
import com.example.booking.domain.payment.PaymentService;
import com.example.booking.support.kafka.KafkaConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatStatusConsumer {

    private final ConcertService concertService;
    private final ObjectMapper objectMapper;
    private final OutboxService outboxService;


    @KafkaListener(topics = KafkaConstants.SEATSTATUS_TOPIC, groupId = "concert")
    public void handleSeatStatusChange(String outboxId, String message) {
        try {
            log.info("Received SEATSTATUS_TOPIC message: {}", message);
            //이벤트오면 id 변경
            outboxService.updateSeatStatus(Long.valueOf(outboxId));
            SeatStatusChangeEvent event = objectMapper.readValue(message, SeatStatusChangeEvent.class);
            Reservation reservation = event.getReservation();

            // 좌석 상태 변경 로직 처리
            concertService.changeStatus(reservation.getSeat());

        } catch (Exception e) {
            log.error("Failed to process message from SEATSTATUS_TOPIC: {}", e.getMessage());
        }
    }
}
