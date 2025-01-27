package com.example.booking.domain.concert;

import com.example.booking.infra.concert.entity.ConcertEntity;

import java.util.List;

public interface ConcertRepository {

    List<Concert> findAll();

    void deleteAll();

    ConcertEntity save(ConcertEntity concert);
}
