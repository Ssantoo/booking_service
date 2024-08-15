package com.example.booking.application.listener;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.event.OutboxService;
import com.example.booking.domain.event.ReservationEvent;
import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.payment.Payment;
import com.example.booking.domain.payment.PaymentService;
import com.example.booking.domain.user.User;
import com.example.booking.infra.concert.ReservationMockApiClient;
import com.example.booking.support.kafka.KafkaConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.booking.support.kafka.service.KafkaProducer;



import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final KafkaProducer kafkaProducer;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutboxReserve(ReservationEvent event) {
        // Outbox 데이터 등록
        ReservationOutbox outbox = outboxService.save(event);
        // set outboxId
        event.setOutboxId(outbox.getOutboxId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservedEvent(ReservationEvent event) throws JsonProcessingException {
        // 예약 완료 kafka 발행
        String eventJson = objectMapper.writeValueAsString(event);
        kafkaProducer.publish(KafkaConstants.RESERVATION_TOPIC, event.getOutboxId(), eventJson);
    }

}
