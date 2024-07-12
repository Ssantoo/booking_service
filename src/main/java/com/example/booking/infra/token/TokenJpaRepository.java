package com.example.booking.infra.token;

import com.example.booking.infra.token.entity.TokenEntity;
import com.example.booking.infra.token.entity.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);
    List<TokenEntity> findByStatus(TokenStatus status);

}
