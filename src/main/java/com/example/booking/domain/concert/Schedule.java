package com.example.booking.domain.concert;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Schedule {

    private final Long id;
    private final LocalDateTime dateTime;
    private final int totalSeats;
    private final int availableSeats;
    private final Concert concert;

    public Schedule(Long id, LocalDateTime dateTime, int totalSeats, int availableSeats, Concert concert) {
        this.id = id;
        this.dateTime = dateTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.concert = concert;
    }

    public boolean isAvailable() {
        return this.availableSeats > 0;
    }


}
