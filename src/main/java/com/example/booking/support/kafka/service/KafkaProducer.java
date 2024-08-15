package com.example.booking.support.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String topic, String jsonData) {
        log.info("-------보낸 메세지-------: '{}' to topic: '{}'", jsonData, topic);
        try {
            kafkaTemplate.send(topic, jsonData);
        } catch (Exception e) {
            log.error("sending to topic={}, error = {}", topic, e.getMessage());
        }
    }
}
