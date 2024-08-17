package com.example.booking.kafka;


import com.example.booking.domain.event.OutboxService;
import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.event.SeatStatusOutbox;
import com.example.booking.infra.concert.entity.SeatStatusOutboxStatus;
import com.example.booking.infra.concert.entity.SeatStatusOutboxType;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import com.example.booking.infra.payment.entity.ReservationOutboxType;
import com.example.booking.support.kafka.KafkaConstants;
import com.example.booking.support.kafka.service.KafkaProducer;
import com.example.booking.support.kafka.service.OutboxRetryScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class OutboxRetrySchedulerTest {

    @Autowired
    private OutboxRetryScheduler outboxRetryScheduler;

    @MockBean
    private OutboxService outboxService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void 예약_메시지_재시도_테스트() {
        // given
        ReservationOutbox reservationOutbox = ReservationOutbox.builder()
                .outboxId(1L)
                .jsonData("sample data")
                .retryCount(2)
                .type(ReservationOutboxType.RESERVE_PAYMENT)
                .status(ReservationOutboxStatus.RETRY)
                .build();

        given(outboxService.findAllReservationOutboxesToRetry()).willReturn(List.of(reservationOutbox));

        // when
        outboxRetryScheduler.retryFailedReservationMessages();

        // then
        verify(kafkaProducer, times(1)).publish(KafkaConstants.RESERVATION_TOPIC, reservationOutbox.getOutboxId(), reservationOutbox.getJsonData());
        verify(outboxService, times(1)).incrementReservation(reservationOutbox);
    }

    @Test
    void 좌석상태_변경_메시지_재시도_테스트() {
        // given
        SeatStatusOutbox seatStatusOutbox = SeatStatusOutbox.builder()
                .outboxId(2L)
                .jsonData("seat status data")
                .retryCount(1)
                .type(SeatStatusOutboxType.RESERVE)
                .status(SeatStatusOutboxStatus.RETRY)
                .build();

        given(outboxService.findAllSeatStatusOutboxesToRetry()).willReturn(List.of(seatStatusOutbox));

        // when
        outboxRetryScheduler.retryFailedSeatStatusMessages();

        // then
        verify(kafkaProducer, times(1)).publish(KafkaConstants.RESERVATION_TOPIC, seatStatusOutbox.getOutboxId(), seatStatusOutbox.getJsonData());
        verify(outboxService, times(1)).incrementSeatStatus(seatStatusOutbox);
    }

    @Test
    void 예약메시지_최대_재시도_테스트() {
        // given
        ReservationOutbox reservationOutbox = ReservationOutbox.builder()
                .outboxId(3L)
                .jsonData("sample data")
                .retryCount(3)
                .type(ReservationOutboxType.RESERVE_PAYMENT)
                .status(ReservationOutboxStatus.RETRY)
                .build();

        given(outboxService.findAllReservationOutboxesToRetry()).willReturn(List.of(reservationOutbox));

        // when
        outboxRetryScheduler.retryFailedReservationMessages();

        // then
        verify(outboxService, times(1)).updateReservationFail(reservationOutbox.getOutboxId());
        verify(kafkaProducer, times(0)).publish(anyString(), anyLong(), anyString());
    }

    @Test
    void 좌석상태_변경_메시지_최대_재시도_테스트() {
        // given
        SeatStatusOutbox seatStatusOutbox = SeatStatusOutbox.builder()
                .outboxId(4L)
                .jsonData("seat status data")
                .retryCount(3)
                .type(SeatStatusOutboxType.RESERVE)
                .status(SeatStatusOutboxStatus.RETRY)
                .build();

        given(outboxService.findAllSeatStatusOutboxesToRetry()).willReturn(List.of(seatStatusOutbox));

        // when
        outboxRetryScheduler.retryFailedSeatStatusMessages();

        // then
        verify(outboxService, times(1)).updateSeatStatusFail(seatStatusOutbox.getOutboxId());
        verify(kafkaProducer, times(0)).publish(anyString(), anyLong(), anyString());
    }
}
