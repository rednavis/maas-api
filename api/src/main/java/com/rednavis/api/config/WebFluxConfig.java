package com.rednavis.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
// https://github.com/springdoc/springdoc-openapi/issues/361
@EnableWebFlux
public class WebFluxConfig {

}