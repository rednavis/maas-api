package com.rednavis.database.config;

import com.rednavis.core.dto.CurrentUserDetails;
import com.rednavis.core.service.CurrentUserService;
import com.rednavis.database.repository.GlobalReactiveMongoRepositoryImpl;
import com.rednavis.database.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = Repository.class, repositoryBaseClass = GlobalReactiveMongoRepositoryImpl.class)
@EnableMongoAuditing
public class MongoConfig {

  @Autowired
  private CurrentUserService currentUserService;

  @Bean
  public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @Bean
  public AuditorAware<CurrentUserDetails> auditor() {
    return () -> currentUserService.getCurrentUser()
        .blockOptional();
  }
}