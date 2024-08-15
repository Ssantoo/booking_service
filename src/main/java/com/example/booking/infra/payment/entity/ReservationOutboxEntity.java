package com.example.booking.infra.payment.entity;

import com.example.booking.domain.event.ReservationOutbox;
import com.example.booking.domain.user.User;
import com.example.booking.infra.user.entity.UserEntity;
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
public class ReservationOutboxEntity extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationOutboxType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationOutboxStatus status;

    private String jsonData;

    private int retryCount = 0;

    @Builder
    public ReservationOutboxEntity(Long outboxId, ReservationOutboxType type, ReservationOutboxStatus status, String jsonData) {
        this.outboxId = outboxId;
        this.type = type;
        this.status = status;
        this.jsonData = jsonData;
        this.retryCount = 0;

    }

    public ReservationOutbox toModel(){
        return ReservationOutbox.builder()
                .type(type)
                .status(status)
                .jsonData(jsonData)
                .build();
    }

    public static ReservationOutboxEntity from(ReservationOutbox reservationOutbox) {
        return ReservationOutboxEntity.builder()
                .type(reservationOutbox.getType())
                .status(reservationOutbox.getStatus())
                .jsonData(reservationOutbox.getJsonData())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationOutboxEntity that = (ReservationOutboxEntity) o;
        return Objects.equals(outboxId, that.outboxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outboxId);
    }
}
