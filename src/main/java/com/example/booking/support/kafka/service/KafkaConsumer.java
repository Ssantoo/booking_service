package com.example.booking.support.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "test-topic", groupId = "concert")
    public void listen(String message) {
        log.info("-------메세지 받기-------: {}", message);
    }

}
