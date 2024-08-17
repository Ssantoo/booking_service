package com.example.booking.application.listener;

import com.example.booking.domain.concert.ConcertService;
import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.event.OutboxService;
import com.example.booking.domain.event.SeatStatusChangeEvent;
import com.example.booking.domain.event.SeatStatusOutbox;
import com.example.booking.support.kafka.KafkaConstants;
import com.example.booking.support.kafka.service.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class SeatStatusChangeEventListener {

    private final ConcertService concertService;
    private final KafkaProducer kafkaProducer;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutboxSeatStatusChange(SeatStatusChangeEvent event) {
        // Outbox에 좌석 상태 변경 이벤트 저장
        SeatStatusOutbox outbox = outboxService.saveSeatStatus(event);
        // Outbox ID 설정
        event.setOutboxId(outbox.getOutboxId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSeatStatusChanged(SeatStatusChangeEvent event) throws JsonProcessingException {
        // 좌석 상태 변경 이벤트 Kafka 발행
        String eventJson = objectMapper.writeValueAsString(event);
        kafkaProducer.publish(KafkaConstants.SEATSTATUS_TOPIC, event.getOutboxId(), eventJson);
    }


}
