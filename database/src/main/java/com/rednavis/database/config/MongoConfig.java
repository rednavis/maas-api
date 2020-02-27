package com.rednavis.database.config;

import com.rednavis.core.service.CurrentUserService;
import com.rednavis.database.repository.GlobalReactiveMongoRepositoryImpl;
import com.rednavis.database.repository.Repository;
import com.rednavis.shared.security.CurrentUser;
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

  @Bean
  public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @Bean
  public AuditorAware<CurrentUser> auditor(CurrentUserService currentUserService) {
    return () -> currentUserService.getCurrentUser()
        .blockOptional();
  }
}