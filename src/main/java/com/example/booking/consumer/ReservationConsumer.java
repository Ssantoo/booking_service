package com.example.booking.consumer;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.event.OutboxService;
import com.example.booking.domain.event.ReservationEvent;
import com.example.booking.domain.payment.PaymentService;
import com.example.booking.domain.user.User;
import com.example.booking.infra.concert.ReservationMockApiClient;
import com.example.booking.support.kafka.KafkaConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationConsumer {

    private final ReservationMockApiClient reservationMockApiClient;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final OutboxService outboxService;

    @KafkaListener(topics = KafkaConstants.RESERVATION_TOPIC, groupId = "concert")
    public void reserved(String outboxId, String message) {
        try {
            log.info("Received RESERVATION_TOPIC: {}", outboxId);

            ReservationEvent event = objectMapper.readValue(message, ReservationEvent.class);
            Reservation reservation = event.getReservation();
            User user = event.getUser();

            //이벤트 받았으니 status 바꿔주기
            outboxService.updateStatus(Long.valueOf(outboxId));

            // 예약 정보 전송
            reservationMockApiClient.sendData(reservation, user);

            //완료후 포인트 차감
            paymentService.pay(user, reservation);

        } catch (Exception e) {
            log.error("메세지 실패: {}", e.getMessage());
        }
    }


}
