package com.rednavis.api;

import static java.util.Collections.singletonList;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@Tag("IntegrationTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MaasApiApplication.class})
public abstract class MaasApiApplicationTest {

  @Container
  private MongoDbContainer mongoDbContainer = new MongoDbContainer();

  /**
   * mongoClient.
   *
   * @return
   */
  @Bean
  public MongoClient mongoClient() {
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyToClusterSettings(settings -> {
          settings.hosts(singletonList(new ServerAddress(mongoDbContainer.getContainerIpAddress(), mongoDbContainer.getPort())));
        })
        .build();
    return MongoClients.create(mongoClientSettings);
  }

  /**
   * mongoTemplate.
   *
   * @param mongoClient mongoClient
   * @return
   */
  @Bean
  public MongoTemplate mongoTemplate(MongoClient mongoClient) {
    return new MongoTemplate(mongoClient, "test");
  }
}