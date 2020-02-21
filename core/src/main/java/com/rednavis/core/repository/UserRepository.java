package com.rednavis.core.repository;

import com.rednavis.core.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, Long> {

  Optional<UserEntity> findById(String id);

  Optional<UserEntity> findByEmail(String email);

  Long deleteById(String id);

  boolean existsByEmail(String email);
}