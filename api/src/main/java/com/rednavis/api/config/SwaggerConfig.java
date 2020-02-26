package com.rednavis.api.config;

import static com.rednavis.shared.RestUrlUtils.AUTH_URL_PATTERN;
import static com.rednavis.shared.RestUrlUtils.USER_URL_PATTERN;

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
    name = "bearerAuth",
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

  private static final String[] PACKAGED_TO_MATCH = {"com.rednavis.api.controller"};

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