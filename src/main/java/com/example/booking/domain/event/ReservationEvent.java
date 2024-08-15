package com.example.booking.domain.event;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ReservationEvent extends ApplicationEvent {

    private final Reservation reservation;
    private Long outboxId;
    private final User user;

    public ReservationEvent(Object source, Long outboxId, Reservation reservation, User user) {
        super(source);
        this.outboxId = outboxId;
        this.reservation = reservation;
        this.user = user;
    }

    public Long getOutboxId() { return outboxId; }

    public Reservation getReservation() {
        return reservation;
    }

    public User getUser() {
        return user;
    }

}
