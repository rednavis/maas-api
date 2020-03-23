package com.rednavis.auth.config;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_PATTERN;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_PATTERN;

import java.util.List;

import com.rednavis.auth.security.JwtReactiveAuthenticationManager;
import com.rednavis.auth.security.JwtServerSecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  private static final List<String> AUTH_WHITELIST = List.of(
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/v3/api-docs/**",
      "/webjars/**",
      "/",
      "/favicon.ico",
      "/**/*.png",
      "/**/*.gif",
      "/**/*.svg",
      "/**/*.jpg",
      "/**/*.html",
      "/**/*.css",
      "/**/*.js"
  );

  /**
   * securityWebFilterChain.
   *
   * @param http http
   * @return
   */
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
      JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager,
      JwtServerSecurityContextRepository jwtServerSecurityContextRepository) {
    return http
        .headers().frameOptions().disable()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(
            (exchange, exception) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
        .accessDeniedHandler((exchange, exception) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
        .and()
        .cors().disable()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .logout().disable()
        .authenticationManager(jwtReactiveAuthenticationManager)
        .securityContextRepository(jwtServerSecurityContextRepository)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers(AUTH_URL_PATTERN).permitAll()
        .pathMatchers(BOOK_URL_PATTERN).permitAll()
        .pathMatchers(AUTH_WHITELIST.toArray(new String[AUTH_WHITELIST.size()])).permitAll()
        .anyExchange().authenticated().and()
        .build();
  }
}