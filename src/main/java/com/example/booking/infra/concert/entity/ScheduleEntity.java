package com.example.booking.infra.concert.entity;

import com.example.booking.domain.concert.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dateTime;

    @Column
    private int totalSeats;

    @Column
    private int availableSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private ConcertEntity concert;

    public Schedule toModel() {
        return Schedule.builder()
                .id(id)
                .dateTime(dateTime)
                .build();
    }

    public static ScheduleEntity from(Schedule schedule){
        return ScheduleEntity.builder()
                .id(schedule.getId())
                .dateTime(schedule.getDateTime())
                .availableSeats(schedule.getAvailableSeats())
                .concert(ConcertEntity.from(schedule.getConcert()))
                .build();
    }

}
