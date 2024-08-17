package com.example.booking.domain.event;

import com.example.booking.domain.concert.Reservation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SeatStatusChangeEvent extends ApplicationEvent {

    private final Reservation reservation;
    private Long outboxId;

    public SeatStatusChangeEvent(Object source, Reservation reservation, Long outboxId) {
        super(source);
        this.reservation = reservation;
        this.outboxId = outboxId;
    }

    public Long getOutboxId() { return outboxId; }
    public Reservation getReservation() {
        return reservation;
    }
}