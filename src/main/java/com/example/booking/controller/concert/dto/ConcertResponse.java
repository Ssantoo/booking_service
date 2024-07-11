package com.example.booking.controller.concert.dto;

import com.example.booking.domain.concert.Concert;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ConcertResponse {
    private final Long id;
    private final String name;
    private final String information;

    public ConcertResponse(Long id, String name, String information) {
        this.id = id;
        this.name = name;
        this.information = information;
    }

    public static ConcertResponse from(Concert concert) {
        return ConcertResponse.builder()
                .id(concert.getId())
                .name(concert.getName())
                .information(concert.getInformation())
                .build();
    }

    public static List<ConcertResponse> from(List<Concert> concerts) {
        return concerts.stream().map(ConcertResponse::from).collect(Collectors.toList());
    }

}
