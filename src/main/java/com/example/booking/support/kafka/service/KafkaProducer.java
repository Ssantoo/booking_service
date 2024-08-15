package com.example.booking.support.kafka.service;

import com.example.booking.domain.event.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxService outboxService;

    public void publish(String topic, Long outboxId, String jsonData) {
        log.info("-------보낸 메세지-------: jsonData={} to topic={}, at outboxId={}", jsonData, topic, outboxId);
        try {
            kafkaTemplate.send(topic, jsonData);
        } catch (Exception e) {
            log.error("sending to topic={}, at outboxId={}, error = {}", topic, outboxId, e.getMessage());
            outboxService.toRetry(outboxId);
        }
    }
}
