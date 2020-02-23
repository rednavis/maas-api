package com.rednavis.auth.config;

import static com.rednavis.core.option.RestOption.AUTH_WHITELIST;

import com.rednavis.auth.security.AuthenticationManager;
import com.rednavis.auth.security.CustomUserDetailsService;
import com.rednavis.auth.security.SecurityContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private SecurityContextRepository securityContextRepository;

  /**
   * securityWebFilterChain.
   *
   * @param http http
   * @return
   */
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .exceptionHandling()
        .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
          swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        })).accessDeniedHandler((swe, e) -> {
          return Mono.fromRunnable(() -> {
            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
          });
        }).and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/login").permitAll()
        .pathMatchers(AUTH_WHITELIST.toArray(new String[AUTH_WHITELIST.size()])).permitAll()
        .anyExchange().authenticated()
        .and().build();
  }

  //@Bean
  //public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
  //  http
  //      .httpBasic()
  //      .and()
  //.cors()
  //.and()
  //.csrf()
  //.disable()
  //.headers()
  //.frameOptions()
  //.disable()
  //.and()
  //.exceptionHandling()
  //.authenticationEntryPoint((exchange, exception) -> {
  //  log.error("Responding with unauthorized error. Message - {}", exception.getMessage());
  //  return Mono.error(new AuthenticationException("Sorry, You're not authorized to access this resource."));
  //})
  //.and()
  //.sessionManagement()
  //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
  //.and()
  //.authorizeExchange()
  //.pathMatchers(AUTH_URL_PATTERN)
  //.permitAll()
  //.pathMatchers(AUTH_WHITELIST.toArray(new String[AUTH_WHITELIST.size()]))
  //.permitAll()
  //.anyExchange()
  //.authenticated();
  //return http.build();

  //http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  //http.csrf()
  //    .disable()
  //    .authorizeExchange()
  //    .pathMatchers(HttpMethod.POST, "/employees/update")
  //    .hasRole("ADMIN")
  //    .pathMatchers("/**")
  //    .permitAll()
  //    .and()
  //    .httpBasic();
  //return http.build();
  //}

  //@Override
  //protected void configure(HttpSecurity http) throws Exception {
  //  http
  //      //.httpBasic()
  //      //.and()
  //      .cors()
  //      .and()
  //      .csrf()
  //      .disable()
  //      .headers()
  //      .frameOptions()
  //      .disable()
  //      .and()
  //      .exceptionHandling()
  //      .authenticationEntryPoint(unauthorizedHandler)
  //      .and()
  //      .sessionManagement()
  //      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
  //      .and()
  //      .authorizeRequests()
  //      .antMatchers("/",
  //          "/favicon.ico",
  //          "/**/ *.png ",
  //          "/**/*.gif",
  //          "/**/*.svg",
  //          "/**/*.jpg",
  //          "/**/*.html",
  //          "/**/*.css",
  //          "/**/*.js")
  //      .permitAll()
  //      .antMatchers("/api/auth/**")
  //      .permitAll()
  //      //.antMatchers("/api/yourEndpoint")
  //      //.permitAll()
  //      .antMatchers(AUTH_WHITELIST)
  //      .permitAll()
  //      .anyRequest()
  //      .authenticated();
  //  http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  //}
}