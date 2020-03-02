package com.rednavis.api;

import com.rednavis.database.repository.RefreshTokenRepository;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Getter
@Tag("IntegrationTest")
@Testcontainers
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MaasApiApplication.class})
@ContextConfiguration(initializers = {MaasApiApplicationTest.Initializer.class})
public abstract class MaasApiApplicationTest {

  @Container
  public static final MongoDbContainer MONGO_DB_CONTAINER = new MongoDbContainer();
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  /**
   * cleanUp.
   */
  @BeforeEach
  public void cleanUp() {
    Mono<Void> deleteCol = refreshTokenRepository.deleteAll();

    StepVerifier
        .create(deleteCol)
        .verifyComplete();
  }

  static class Initializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
          "spring.data.mongodb.host=" + MONGO_DB_CONTAINER.getContainerIpAddress(),
          "spring.data.mongodb.port=" + MONGO_DB_CONTAINER.getPort()
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}