package com.example.booking.support.kafka.service;

import com.example.booking.domain.event.OutboxService;
import com.example.booking.support.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerListener implements ProducerListener<String, String> {

    private final OutboxService outboxService;

    @Override
    public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("카프카 메세지 전송 실패. Topic: {}, Key: {}, Exception: {}",
                recordMetadata.topic(), producerRecord.key(), exception.getMessage());

        Long outboxId = Long.valueOf(producerRecord.key());

        try {
            if (recordMetadata.topic().equals(KafkaConstants.RESERVATION_TOPIC)) {
                outboxService.updateStatus(outboxId);
            } else if (recordMetadata.topic().equals(KafkaConstants.SEATSTATUS_TOPIC)) {
                outboxService.updateSeatStatus(outboxId);
            }
        } catch (Exception e) {
            log.error("Failed to update Outbox status for Outbox ID: {}", outboxId, e);
        }
    }
}
