package com.example.booking.infra.token;

import com.example.booking.infra.token.entity.TokenEntity;
import com.example.booking.infra.token.entity.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);
    List<TokenEntity> findByStatus(TokenStatus status);

    int countByStatus(TokenStatus status);

    @Query("SELECT t FROM TokenEntity t WHERE t.status = :status ORDER BY t.createdAt ASC")
    List<TokenEntity> findTopNByStatusOrderByCreatedAt(@Param("status") TokenStatus status, @Param("n") int n);

    Optional<TokenEntity> findAllByOrderByCreatedAt();

    @Query(value = "SELECT * FROM token t WHERE t.created_at + INTERVAL '5 MINUTE'", nativeQuery = true)
    List<TokenEntity> findActiveWithinLastMinutes();


}
