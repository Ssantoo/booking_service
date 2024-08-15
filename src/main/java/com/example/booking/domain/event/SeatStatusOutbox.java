package com.example.booking.domain.event;

import com.example.booking.infra.concert.entity.SeatStatusOutboxStatus;
import com.example.booking.infra.concert.entity.SeatStatusOutboxType;
import com.example.booking.infra.payment.entity.ReservationOutboxStatus;
import com.example.booking.infra.payment.entity.ReservationOutboxType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatStatusOutbox {

    private Long outboxId;

    private SeatStatusOutboxType type;

    private SeatStatusOutboxStatus status;

    private String jsonData;

    private int retryCount = 0;

    public SeatStatusOutbox(Long outboxId, SeatStatusOutboxType type, SeatStatusOutboxStatus status, String jsonData, int retryCount) {
        this.outboxId = outboxId;
        this.type = type;
        this.status = status;
        this.jsonData = jsonData;
        this.retryCount = retryCount;
    }

    public static SeatStatusOutbox create(String jsonData) {
        return SeatStatusOutbox.builder()
                .type(SeatStatusOutboxType.RESERVE)
                .status(SeatStatusOutboxStatus.INIT)
                .jsonData(jsonData)
                .build();
    }


    public void updateStatus(SeatStatusOutboxStatus newStatus) {
        this.status = newStatus;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public void update() {
        this.status = SeatStatusOutboxStatus.DONE;
    }
}
