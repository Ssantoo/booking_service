package com.example.booking.domain.event;

import com.example.booking.infra.concert.entity.SeatStatus;
import com.example.booking.infra.concert.entity.SeatStatusOutboxType;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import com.example.booking.infra.payment.entity.ReservationOutboxType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationOutbox {

    private Long outboxId;

    private ReservationOutboxType type;

    private ReservationOutboxStatus status;

    private String jsonData;

    private int retryCount = 0;

    public ReservationOutbox(Long outboxId, ReservationOutboxType type, ReservationOutboxStatus status, String jsonData, int retryCount) {
        this.outboxId = outboxId;
        this.type = type;
        this.status = status;
        this.jsonData = jsonData;
        this.retryCount = retryCount;
    }

    public void updateStatus(ReservationOutboxStatus newStatus) {
        this.status = newStatus;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public static ReservationOutbox create(String jsonData) {
        return ReservationOutbox.builder()
                .type(ReservationOutboxType.RESERVE_PAYMENT)
                .status(ReservationOutboxStatus.INIT)
                .jsonData(jsonData)
                .build();
    }

    public void update() {
        this.status = ReservationOutboxStatus.DONE;
    }
}
