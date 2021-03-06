package com.rednavis.api;

import java.util.Set;
import com.rednavis.auth.AuthModule;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.CoreModule;
import com.rednavis.database.DatabaseModule;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
@Import({AuthModule.class,
    CoreModule.class,
    DatabaseModule.class})
@RequiredArgsConstructor
public class MaasApiApplication {

  public static final String ADMIN_EMAIL = "admin@admin.com";
  public static final String ADMIN_USERNAME = "admin";
  public static final String ADMIN_PASSWORD = "admin";
  public static final String USER_EMAIL = "user@user.com";
  public static final String USER_USERNAME = "user";
  public static final String USER_PASSWORD = "user";

  private final PasswordService passwordService;
  private final UserService userService;

  public static void main(String... args) {
    SpringApplication.run(MaasApiApplication.class, args);
  }

  @Component
  class ContextStartedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
      log.info("ContextRefreshedEvent Initialization logic ...");
      userService.findByEmail(ADMIN_EMAIL)
          .doOnNext(admin -> log.info("Default admin has been found [admin: {}]", admin))
          .switchIfEmpty(
              Mono.defer(() -> {
                log.info("There is no default admin about your search criteria [criteria: {}]", ADMIN_EMAIL);
                return Mono.just(createAdmin());
              })
                  .flatMap(userService::save))
          .subscribe();
      userService.findByEmail(USER_EMAIL)
          .doOnNext(user -> log.info("Default user has been found [user: {}]", user))
          .switchIfEmpty(
              Mono.defer(() -> {
                log.info("There is no default user about your search criteria [criteria: {}]", USER_EMAIL);
                return Mono.just(createUser());
              })
                  .flatMap(userService::save))
          .subscribe();
    }

    private User createAdmin() {
      String passwordToken = passwordService.generatePassword(ADMIN_PASSWORD);
      return User.builder()
          .firstName(User.Fields.firstName)
          .lastName(User.Fields.lastName)
          .email(ADMIN_EMAIL)
          .userName(ADMIN_USERNAME)
          .password(passwordToken)
          .roles(Set.of(RoleEnum.ROLE_ADMIN))
          .build();
    }

    private User createUser() {
      String passwordToken = passwordService.generatePassword(USER_PASSWORD);
      return User.builder()
          .firstName(User.Fields.firstName)
          .lastName(User.Fields.lastName)
          .email(USER_EMAIL)
          .userName(USER_USERNAME)
          .password(passwordToken)
          .roles(Set.of(RoleEnum.ROLE_USER))
          .build();
    }
  }
}
