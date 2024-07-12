package com.example.booking.infra.concert.entity;

import com.example.booking.domain.concert.Reservation;
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
@Table(name = "reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long concertScheduleId;

//    @Column(nullable = false)
//    private Long seatId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", insertable = false, updatable = false)
    private SeatEntity seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private int totalPrice;

    public Reservation toModel() {
        return Reservation.builder()
                .id(id)
                .userId(userId)
                .concertScheduleId(concertScheduleId)
                .seat(seat.toModel())
                .status(status)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .totalPrice(totalPrice)
                .build();
    }

    public static ReservationEntity from(Reservation reservation) {
        return ReservationEntity.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .concertScheduleId(reservation.getConcertScheduleId())
                .seat(SeatEntity.from(reservation.getSeat()))
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .totalPrice(reservation.getTotalPrice())
                .build();
    }
}
