package com.rednavis.api.config;

import static com.rednavis.api.config.SwaggerConfig.BEARER_AUTH;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_PATTERN;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_PATTERN;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_PATTERN;

import com.rednavis.api.controller.AuthController;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.Builder;
import lombok.Getter;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = BEARER_AUTH,
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@OpenAPIDefinition(
    info = @Info(
        title = "Maas API",
        description = "MAAS-API REST API description",
        version = "v1",
        license = @License(
            name = "GNU General Public License v3",
            url = "https://www.gnu.org/licenses/gpl-3.0.html"
        )
    ))
@SuppressWarnings("AbbreviationAsWordInName")
public class SwaggerConfig {

  public static final String BEARER_AUTH = "bearerAuth";

  private static final String[] PACKAGED_TO_MATCH = {AuthController.class
      .getPackage()
      .getName()};

  /**
   * authOpenApi.
   *
   * @return
   */
  @Bean
  public GroupedOpenApi authOpenApi() {
    GroupInfo groupInfo = GroupInfo.builder()
        .paths(new String[] {AUTH_URL_PATTERN})
        .group("auth")
        .build();
    return createNewGroup(groupInfo);
  }

  /**
   * userOpenApi.
   *
   * @return
   */
  @Bean
  public GroupedOpenApi userOpenApi() {
    GroupInfo groupInfo = GroupInfo.builder()
        .paths(new String[] {USER_URL_PATTERN})
        .group("user")
        .build();
    return createNewGroup(groupInfo);
  }

  /**
   * bookOpenApi.
   *
   * @return
   */
  @Bean
  public GroupedOpenApi bookOpenApi() {
    GroupInfo groupInfo = GroupInfo.builder()
        .paths(new String[] {BOOK_URL_PATTERN})
        .group("book")
        .build();
    return createNewGroup(groupInfo);
  }

  private GroupedOpenApi createNewGroup(GroupInfo groupInfo) {
    return GroupedOpenApi.builder()
        .setGroup(groupInfo.getGroup())
        .pathsToMatch(groupInfo.getPaths())
        .packagesToScan(PACKAGED_TO_MATCH)
        .build();
  }

  @Getter
  @Builder
  static class GroupInfo {

    private String[] paths;
    private String group;
  }
}