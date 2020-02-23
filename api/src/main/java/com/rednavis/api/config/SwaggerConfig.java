package com.rednavis.api.config;

import static com.rednavis.core.option.RestOption.AUTH_URL_PATTERN;
import static com.rednavis.core.option.RestOption.USER_URL_PATTERN;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Builder;
import lombok.Getter;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

  /**
   * customOpenAPI.
   *
   * @param appVersion appVersion
   * @return
   */
  @Bean
  public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
    return new OpenAPI()
        .components(new Components().addSecuritySchemes("basicScheme",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
        .info(new Info().title("Maas API")
            .version(appVersion)
            .description("MAAS-API REST API description")
            .license(new License().name("GNU General Public License v3")
                .url("https://www.gnu.org/licenses/gpl-3.0.html")));
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