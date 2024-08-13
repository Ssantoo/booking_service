package com.example.booking.domain.event;

import com.example.booking.domain.concert.Reservation;
import org.springframework.context.ApplicationEvent;

public class SeatStatusChangeEvent extends ApplicationEvent {

    private final Reservation reservation;

    public SeatStatusChangeEvent(Object source, Reservation reservation) {
        super(source);
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}