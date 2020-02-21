package com.rednavis.core.repository;

import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;

@NoRepositoryBean
@SuppressWarnings("InterfaceTypeParameterName")
public interface GlobalReactiveMongoRepository<T, ID extends Serializable> extends ReactiveMongoRepository<T, ID> {

  Flux<T> findAll(Pageable pageable);
}
