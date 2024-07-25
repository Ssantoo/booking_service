package com.example.booking.infra.concert.entity;

import com.example.booking.domain.concert.Seat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seat")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    //추후에 추가
//    @Column(name = "section")
//    private String section;
//
//    @Column(name = "row")
//    private String row;

    @Column(name = "seat_number")
    private int seatNuamber;

    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status;

    @Version
    private int version;

    public Seat toModel() {
        return Seat.builder()
                .id(id)
                .seatNumber(seatNuamber)
                .price(price)
                .status(status)
                .version(version)
                .build();
    }

    public static SeatEntity from(Seat seat) {
        return SeatEntity.builder()
                .id(seat.getId())
                .seatNuamber(seat.getSeatNumber())
                .price(seat.getSeatNumber())
                .status(seat.getStatus())
                .version(seat.getVersion())
                .build();
    }


}
