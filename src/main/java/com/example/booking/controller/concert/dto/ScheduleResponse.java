package com.example.booking.controller.concert.dto;

import com.example.booking.domain.concert.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ScheduleResponse {

    private final Long id;
    private final LocalDateTime dateTime;
    private final int totalSeats;
    private final int availableSeats;
    private final ConcertResponse concert;

    public ScheduleResponse(Long id, LocalDateTime dateTime, int totalSeats ,int availableSeats, ConcertResponse concert){
        this.id = id;
        this.dateTime = dateTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.concert = concert;
    }


    public static  ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getDateTime(),
                schedule.getTotalSeats(),
                schedule.getAvailableSeats(),
                ConcertResponse.from(schedule.getConcert())
        );
    }

    public static List<ScheduleResponse> from(List<Schedule> schedules) {
        return schedules.stream().map(ScheduleResponse::from).collect(Collectors.toList());
    }
}
