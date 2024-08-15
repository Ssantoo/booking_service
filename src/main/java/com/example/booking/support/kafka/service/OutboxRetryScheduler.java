package com.example.booking.support.kafka.service;

import com.example.booking.domain.event.OutboxService;
import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.event.SeatStatusOutbox;
import com.example.booking.support.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRetryScheduler {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 600000) // 10분마다 실행
    public void retryFailedReservationMessages() {
        List<ReservationOutbox> outboxesToRetry = outboxService.findAllReservationOutboxesToRetry();
        if (outboxesToRetry.isEmpty()) {
            return;
        }
        for (ReservationOutbox outbox : outboxesToRetry) {
            if (outbox.getRetryCount() >= 3) {
                outboxService.updateReservationFail(outbox.getOutboxId());
                continue;
            }

            try {
                String topic = null;
                switch (outbox.getType()) {
                    case RESERVE_PAYMENT -> topic = KafkaConstants.RESERVATION_TOPIC;
                    case CANCEL_PAYMENT -> topic = KafkaConstants.CANCEL_TOPIC;
                }

                kafkaProducer.publish(topic, outbox.getOutboxId(), outbox.getJsonData());
            } catch (Exception e) {
                log.error("Reservation 재시도 실패-> outboxId: {}, error: {}", outbox.getOutboxId(), e.getMessage());
            }

            outbox.incrementRetryCount();
            outboxService.incrementReservation(outbox);
        }
    }

    @Scheduled(fixedDelay = 600000) // 10분마다 실행
    public void retryFailedSeatStatusMessages() {
        List<SeatStatusOutbox> outboxesToRetry = outboxService.findAllSeatStatusOutboxesToRetry();
        if (outboxesToRetry.isEmpty()) {
            return;
        }
        for (SeatStatusOutbox outbox : outboxesToRetry) {
            if (outbox.getRetryCount() >= 3) {
                outboxService.updateSeatStatusFail(outbox.getOutboxId());
                continue;
            }

            try {

                String topic = null;
                switch (outbox.getType()) {
                    case RESERVE -> topic = KafkaConstants.RESERVATION_TOPIC;
                    case CANCEL -> topic = KafkaConstants.CANCEL_TOPIC;
                }

                kafkaProducer.publish(topic, outbox.getOutboxId(), outbox.getJsonData());
            } catch (Exception e) {
                log.error("Seat status 변경 실패 -> outboxId: {}, error: {}", outbox.getOutboxId(), e.getMessage());
            }

            outbox.incrementRetryCount();
            outboxService.incrementSeatStatus(outbox);
        }
    }
}
