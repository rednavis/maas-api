package com.rednavis.api;

import com.rednavis.auth.AuthModule;
import com.rednavis.auth.service.password.CannotPerformOperationException;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.CoreModule;
import com.rednavis.core.service.UserService;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import({AuthModule.class,
    CoreModule.class})
public class MaasApiApplication implements InitializingBean {

  private static final String ADMIN_EMAIL = "admin@admin.com";
  private static final String ADMIN_PASSWORD = "1@QWaszx";

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
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("AppInitializator initialization logic ...");
    userService.existsByEmail(ADMIN_EMAIL)
        .subscribe(result -> {
              if (!result) {
                String passwordToken = null;
                try {
                  passwordToken = passwordService.generatePassword(ADMIN_PASSWORD);
                } catch (CannotPerformOperationException ex) {
                  log.error("Can't generate admin password", ex);
                }
                User admin = User.builder()
                    .firstName("adminName")
                    .lastName("adminLastName")
                    .email(ADMIN_EMAIL)
                    .password(passwordToken)
                    .roles(Set.of(RoleEnum.ROLE_ADMIN))
                    .build();
                userService.save(admin);
              }
            }
        );
  }
}
