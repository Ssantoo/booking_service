package com.example.booking.infra.concert.entity;

import com.example.booking.domain.concert.Concert;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "concert")
public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String information;


    public Concert toModel() {
        return Concert.builder()
                .id(id)
                .name(name)
                .information(information)
                .build();
    }

    public static ConcertEntity from(Concert concert){
        return ConcertEntity.builder()
                .id(concert.getId())
                .name(concert.getName())
                .information(concert.getInformation())
                .build();
    }
}
