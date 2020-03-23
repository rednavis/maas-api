package com.rednavis.database.repository;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;

@NoRepositoryBean
public interface GlobalReactiveMongoRepository<T, I extends Serializable> extends ReactiveMongoRepository<T, I> {

  Flux<T> findAll(Pageable pageable);
}
