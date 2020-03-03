package com.rednavis.api;

import com.rednavis.auth.AuthModule;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.CoreModule;
import com.rednavis.database.DatabaseModule;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import java.util.Set;
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
  public static final String ADMIN_PASSWORD = "1@QWaszx";

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
          .switchIfEmpty(Mono.just(createAdmin()))
          .flatMap(admin -> userService.save(admin))
          .subscribe(user -> log.info("Create default admin [admin: {}]", user));
    }

    private User createAdmin() {
      String passwordToken = passwordService.generatePassword(ADMIN_PASSWORD);
      return User.builder()
          .firstName(User.Fields.firstName)
          .lastName(User.Fields.lastName)
          .email(ADMIN_EMAIL)
          .password(passwordToken)
          .roles(Set.of(RoleEnum.ROLE_ADMIN))
          .build();
    }

  }
}
