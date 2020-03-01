package com.rednavis.api;

import com.rednavis.auth.AuthModule;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.CoreModule;
import com.rednavis.database.DatabaseModule;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
@Import({AuthModule.class,
    CoreModule.class,
    DatabaseModule.class})
public class MaasApiApplication implements InitializingBean {

  public static final String ADMIN_EMAIL = "admin@admin.com";
  public static final String ADMIN_PASSWORD = "1@QWaszx";

  @Autowired
  private PasswordService passwordService;
  @Autowired
  private UserService userService;

  public static void main(String... args) {
    SpringApplication.run(MaasApiApplication.class, args);
  }

  /**
   * afterPropertiesSet.
   *
   * @throws Exception exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("AppInitializator initialization logic ...");
    userService.existsByEmail(ADMIN_EMAIL)
        .filter(exist -> !exist)
        .then(Mono.just(passwordService.generatePassword(ADMIN_PASSWORD)))
        .map(this::createAdmin)
        .map(admin -> userService.save(admin))
        .subscribe(user -> log.info("Create default admin [admin: {}]", user));
  }

  private User createAdmin(String passwordToken) {
    return User.builder()
        .firstName(User.Fields.firstName)
        .lastName(User.Fields.lastName)
        .email(ADMIN_EMAIL)
        .password(passwordToken)
        .roles(Set.of(RoleEnum.ROLE_ADMIN))
        .build();
  }
}
