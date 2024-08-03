package com.example.booking.domain.concert;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Concert {
    private final Long id;
    private final String name;
    private final String information;
    private Schedule schedules;

    @Builder
    public Concert(Long id, String name, String information, Schedule schedules) {
        this.id = id;
        this.name = name;
        this.information = information;
        this.schedules = schedules;
    }

    @Builder
    public Concert(Long id, String name, String information) {
        this.id = id;
        this.name = name;
        this.information = information;
    }

}
