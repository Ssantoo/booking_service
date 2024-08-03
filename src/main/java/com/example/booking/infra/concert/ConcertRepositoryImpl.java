package com.example.booking.infra.concert;

import com.example.booking.domain.concert.Concert;
import com.example.booking.domain.concert.ConcertRepository;
import com.example.booking.infra.concert.entity.ConcertEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public List<Concert> findAll() {
        return concertJpaRepository.findAll().stream().map(ConcertEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        concertJpaRepository.deleteAll();
    }

    @Override
    public ConcertEntity save(ConcertEntity concert) {
        return concertJpaRepository.save(concert);
    }
}
