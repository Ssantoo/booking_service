package com.example.booking.domain.event;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;


public class ReservationEvent extends ApplicationEvent {

    private final Reservation reservation;

    private final User user;

    public ReservationEvent(Object source, Reservation reservation, User user) {
        super(source);
        this.reservation = reservation;
        this.user = user;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public User getUser() {
        return user;
    }

}
