package com.rednavis.auth.config;

import static com.rednavis.core.option.RestOption.AUTH_URL;
import static com.rednavis.core.option.RestOption.AUTH_WHITELIST;

import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  //@Autowired
  //private CustomUserDetailsService customUserDetailsService;

  //@Bean
  //public JwtAuthenticationFilter jwtAuthenticationFilter() {
  //  return new JwtAuthenticationFilter();
  //}

  //@Bean(BeanIds.AUTHENTICATION_MANAGER)
  //@Override
  //public AuthenticationManager authenticationManagerBean() throws Exception {
  //  return super.authenticationManagerBean();
  //}

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    UserDetails user = User
        .withUsername("admin")
        .password(passwordEncoder().encode("password"))
        .roles("ADMIN")
        .build();
    return new MapReactiveUserDetailsService(user);
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http
        //.httpBasic()
        //.and()
        .cors()
        .and()
        .csrf()
        .disable()
        .headers()
        .frameOptions()
        .disable()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint((exchange, exception) -> {
          log.error("Responding with unauthorized error. Message - {}", exception.getMessage());
          return Mono.error(new AuthenticationException("Sorry, You're not authorized to access this resource."));
        })
        .and()
        //.sessionManagement()
        //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //.and()
        .authorizeExchange()
        .pathMatchers(AUTH_URL + "/**")
        .permitAll()
        .pathMatchers(AUTH_WHITELIST.toArray(new String[AUTH_WHITELIST.size()]))
        .permitAll()
        .anyExchange()
        .authenticated();
    return http.build();

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
  }

  //@Override
  //public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
  //  authenticationManagerBuilder
  //      .userDetailsService(customUserDetailsService)
  //      .passwordEncoder(passwordEncoder());
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