package com.rednavis.database.repository;

import com.rednavis.database.entity.RefreshTokenEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RefreshTokenRepository extends ReactiveMongoRepository<RefreshTokenEntity, String> {

  Mono<RefreshTokenEntity> findRefreshTokenEntityByRefreshToken(String refreshToken);

  Mono<Boolean> existsByRefreshToken(String refreshToken);
}
