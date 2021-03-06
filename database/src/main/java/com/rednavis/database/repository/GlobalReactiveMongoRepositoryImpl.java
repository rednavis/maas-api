package com.rednavis.database.repository;

import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import reactor.core.publisher.Flux;

@SuppressWarnings("ClassTypeParameterName")
public class GlobalReactiveMongoRepositoryImpl<T, I extends Serializable> extends SimpleReactiveMongoRepository<T, I> implements
    GlobalReactiveMongoRepository<T, I> {

  private MongoEntityInformation<T, I> entityInformation;
  private ReactiveMongoOperations mongoOperations;

  /**
   * GlobalReactiveMongoRepositoryImpl.
   *
   * @param entityInformation entityInformation
   * @param mongoOperations   mongoOperations
   */
  public GlobalReactiveMongoRepositoryImpl(MongoEntityInformation<T, I> entityInformation, ReactiveMongoOperations mongoOperations) {
    super(entityInformation, mongoOperations);
    this.entityInformation = entityInformation;
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Flux<T> findAll(Pageable pageable) {
    return mongoOperations.find(new Query().with(pageable), entityInformation.getJavaType(), entityInformation.getCollectionName());
  }
}