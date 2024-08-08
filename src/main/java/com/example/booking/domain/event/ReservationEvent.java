package com.example.booking.domain.event;

import com.example.booking.domain.concert.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;


public class ReservationEvent extends ApplicationEvent {

    private final Reservation reservation;

    public ReservationEvent(Object source, Reservation reservation) {
        super(source);
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

}
