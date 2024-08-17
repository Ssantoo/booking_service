package com.example.booking.domain.event;

import com.example.booking.infra.concert.entity.SeatStatusOutboxStatus;
import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final ReservationOutboxRepository reservationOutboxRepository;

    private final SeatStatusOutboxRepository seatStatusOutboxRepository;

    public void toRetry(Long outboxId) {
    }

    public ReservationOutbox save(ReservationEvent event) {
        String jsonData = String.valueOf(event.getReservation());
        return reservationOutboxRepository.save(ReservationOutbox.create(jsonData));
    }

    public ReservationOutbox findByReservationId(Long reservationId) {
        return reservationOutboxRepository.findByReservationId(reservationId);
    }

    public void updateStatus(Long outboxId) {
        ReservationOutbox outbox = reservationOutboxRepository.findById(outboxId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox 찾을수 없습니다 " + outboxId));
        outbox.update();
        reservationOutboxRepository.save(outbox);
    }

    public SeatStatusOutbox saveSeatStatus(SeatStatusChangeEvent event) {
        String jsonData = String.valueOf(event.getReservation());
        return seatStatusOutboxRepository.save(SeatStatusOutbox.create(jsonData));
    }

    public void updateSeatStatus(Long outboxId) {
        SeatStatusOutbox outbox = seatStatusOutboxRepository.findById(outboxId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox 찾을수 없습니다 " + outboxId));
        outbox.update();
        seatStatusOutboxRepository.save(outbox);
    }

    // 재시도할 예약 Outbox 조회
    public List<ReservationOutbox> findAllReservationOutboxesToRetry() {
        return reservationOutboxRepository.findAllByStatus(ReservationOutboxStatus.RETRY);
    }

    // 재시도할 좌석 상태 변경 Outbox 조회
    public List<SeatStatusOutbox> findAllSeatStatusOutboxesToRetry() {
        return seatStatusOutboxRepository.findAllByStatus(SeatStatusOutboxStatus.RETRY);
    }

    //예약 실패
    public void updateReservationFail(Long outboxId) {
        ReservationOutbox outbox = reservationOutboxRepository.findById(outboxId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox 찾을수 없습니다 " + outboxId));
        outbox.fail();
        reservationOutboxRepository.save(outbox);
    }
    //좌석 실패
    public void updateSeatStatusFail(Long outboxId) {
        SeatStatusOutbox outbox = seatStatusOutboxRepository.findById(outboxId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox 찾을수 없습니다 " + outboxId));
        outbox.fail();
        seatStatusOutboxRepository.save(outbox);
    }

    public void incrementReservation(ReservationOutbox outbox) {
        reservationOutboxRepository.incrementReservation(outbox);
    }

    public void incrementSeatStatus(SeatStatusOutbox outbox) {
        seatStatusOutboxRepository.incrementSeatStatus(outbox);
    }
}
