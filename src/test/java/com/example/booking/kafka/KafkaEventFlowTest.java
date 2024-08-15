package com.example.booking.kafka;

import com.example.booking.domain.concert.*;
import com.example.booking.domain.event.OutboxService;
import com.example.booking.domain.event.ReservationEvent;
import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.infra.concert.entity.ReservationStatus;
import com.example.booking.infra.concert.entity.SeatStatus;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import com.example.booking.support.kafka.KafkaConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {KafkaConstants.RESERVATION_TOPIC}, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9093", "port=9093"})
public class KafkaEventFlowTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaConsumerTestListener kafkaConsumerTestListener;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutboxService outboxService;

    @Autowired
    private ConcertService concertService;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Test
    public void 예약_서비스_이벤트_전송_테스트() throws Exception {
         String token = "valid-token";
        Schedule mockSchedule = Schedule.builder()
                .id(1L)
                .dateTime(LocalDateTime.of(2024, 7, 5, 0, 0))
                .totalSeats(100)
                .availableSeats(50)
                .concert(new Concert(1L, "해리포터 1", "마법사의돌"))
                .build();
        Seat seat = new Seat(1L, 1, 100, SeatStatus.AVAILABLE, mockSchedule, 1);
        Reservation reservation = Reservation.builder()
                .id(1L)
                .userId(1L)
                .concertScheduleId(1L)
                .seat(seat)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalPrice(100)
                .build();


        // 예약 서비스 호출 (Kafka로 이벤트 발행)
        concertService.reserve(reservation, token);

        // then: Kafka 메시지가 잘 수신되었는지 확인
        ReservationEvent receivedEvent = kafkaConsumerTestListener.getReceivedEvent();
        assertThat(receivedEvent).isNotNull();
        assertThat(receivedEvent.getReservation().getId()).isEqualTo(reservation.getId());
        assertThat(receivedEvent.getUser().getId()).isEqualTo(reservation.getUserId());

        // 메시지 발행 후 Outbox 상태가 정상적으로 변경되었는지 확인
        ReservationOutbox outbox = outboxService.findByReservationId(reservation.getId());
        assertThat(outbox).isNotNull();
        assertThat(outbox.getStatus()).isEqualTo(ReservationOutboxStatus.DONE);
    }

}
