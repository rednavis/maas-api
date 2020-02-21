package com.rednavis.core.repository;

import com.rednavis.core.entity.UserEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends GlobalReactiveMongoRepository<UserEntity, String> {

  Mono<UserEntity> findByEmail(String email);

  Mono<Boolean> existsByEmail(String email);
}