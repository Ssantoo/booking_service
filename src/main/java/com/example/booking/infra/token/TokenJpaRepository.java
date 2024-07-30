package com.example.booking.infra.token;

import com.example.booking.infra.token.entity.TokenEntity;
import com.example.booking.infra.token.entity.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);
    List<TokenEntity> findByStatus(TokenStatus status);

    int countByStatus(TokenStatus status);

//    @Query("SELECT t FROM TokenEntity t WHERE t.status = :status ORDER BY t.createdAt ASC")
//    List<TokenEntity> findTopNByStatusOrderByCreatedAt(@Param("status") TokenStatus status, @Param("n") int n);
    @Query(value = "SELECT * FROM token_entity t WHERE t.status = :status ORDER BY t.created_at ASC LIMIT :n", nativeQuery = true)
    List<TokenEntity> findTopNByStatusOrderByCreatedAt(@Param("status") TokenStatus status, @Param("n") int n);


    Optional<TokenEntity> findAllByOrderByCreatedAt();
}
