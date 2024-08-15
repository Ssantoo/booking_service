package com.example.booking.kafka;

import com.example.booking.domain.event.ReservationEvent;
import com.example.booking.support.kafka.KafkaConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaConsumerTestListener {

    private final CountDownLatch latch = new CountDownLatch(1);
    private ReservationEvent receivedEvent;

    @KafkaListener(topics = KafkaConstants.RESERVATION_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.receivedEvent = objectMapper.readValue(message, ReservationEvent.class);
        latch.countDown();
    }

    public ReservationEvent getReceivedEvent() throws InterruptedException {
        latch.await(10, TimeUnit.SECONDS);
        return receivedEvent;
    }
}
