package com.example.booking.kafka;

import com.example.booking.domain.event.ReservationEvent;
import com.example.booking.domain.event.SeatStatusChangeEvent;
import com.example.booking.support.kafka.KafkaConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaConsumerTestListener {

    private final CountDownLatch reservationLatch = new CountDownLatch(1);
    private final CountDownLatch seatStatusLatch = new CountDownLatch(1);

    private ReservationEvent receivedReservationEvent;
    private SeatStatusChangeEvent receivedSeatStatusChangeEvent;

    @KafkaListener(topics = KafkaConstants.RESERVATION_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void listenReservation(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.receivedReservationEvent = objectMapper.readValue(message, ReservationEvent.class);
        reservationLatch.countDown();
    }

    @KafkaListener(topics = KafkaConstants.SEATSTATUS_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void listenSeatStatus(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.receivedSeatStatusChangeEvent = objectMapper.readValue(message, SeatStatusChangeEvent.class);
        seatStatusLatch.countDown();
    }

    public ReservationEvent getReceivedReservationEvent() throws InterruptedException {
        reservationLatch.await(10, TimeUnit.SECONDS);
        return receivedReservationEvent;
    }

    public SeatStatusChangeEvent getReceivedSeatStatusChangeEvent() throws InterruptedException {
        seatStatusLatch.await(10, TimeUnit.SECONDS);
        return receivedSeatStatusChangeEvent;
    }
}
