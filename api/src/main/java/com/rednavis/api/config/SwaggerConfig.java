package com.rednavis.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

  /**
   * api.
   *
   * @return
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(metaData())
        .pathMapping("/");
  }

  private ApiInfo metaData() {
    return new ApiInfoBuilder()
        .title("MAAS-API")
        .description("MAAS-API REST API description")
        .version("1.0.0")
        .license("GNU General Public License v3")
        .licenseUrl("https://www.gnu.org/licenses/gpl-3.0.html")
        .build();
  }
}