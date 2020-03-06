package com.rednavis.database.repository;

import com.rednavis.database.entity.UserEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends GlobalReactiveMongoRepository<UserEntity, String> {

  Mono<UserEntity> findByEmail(String email);

  Mono<UserEntity> findByUserName(String userName);
}