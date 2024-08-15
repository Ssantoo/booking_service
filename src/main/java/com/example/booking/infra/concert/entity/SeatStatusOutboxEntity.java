package com.example.booking.infra.concert.entity;

import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.event.SeatStatusOutbox;
import com.example.booking.infra.payment.entity.ReservationOutboxEntity;
import com.example.booking.support.time.BaseDateTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class SeatStatusOutboxEntity extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatusOutboxType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatusOutboxStatus status;

    private String jsonData;

    private int retryCount = 0;

    @Builder
    public SeatStatusOutboxEntity(Long outboxId, SeatStatusOutboxType type, SeatStatusOutboxStatus status, String jsonData) {
        this.outboxId = outboxId;
        this.type = type;
        this.status = status;
        this.jsonData = jsonData;
    }

    public SeatStatusOutbox toModel(){
        return SeatStatusOutbox.builder()
                .type(type)
                .status(status)
                .jsonData(jsonData)
                .build();
    }

    public static SeatStatusOutboxEntity from(SeatStatusOutbox seatStatusOutbox) {
        return SeatStatusOutboxEntity.builder()
                .type(seatStatusOutbox.getType())
                .status(seatStatusOutbox.getStatus())
                .jsonData(seatStatusOutbox.getJsonData())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatStatusOutboxEntity that = (SeatStatusOutboxEntity) o;
        return Objects.equals(outboxId, that.outboxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outboxId);
    }
}
