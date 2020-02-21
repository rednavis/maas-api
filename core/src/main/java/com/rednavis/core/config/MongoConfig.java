package com.rednavis.core.config;

import com.rednavis.core.repository.GlobalReactiveMongoRepositoryImpl;
import com.rednavis.core.repository.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = Repository.class, repositoryBaseClass = GlobalReactiveMongoRepositoryImpl.class)
@EnableMongoAuditing
public class MongoConfig {

  @Bean
  public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }
}