package com.rednavis.api.config;

import static com.rednavis.core.option.RestOption.AUTH_URL_SIGNIN;
import static com.rednavis.core.option.RestOption.AUTH_URL_SIGNUP;
import static com.rednavis.core.option.RestOption.USER_URL_ADMIN;
import static com.rednavis.core.option.RestOption.USER_URL_ME;
import static com.rednavis.core.option.RestOption.USER_URL_USER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.rednavis.api.hadler.UserHandler;
import com.rednavis.auth.hadler.AuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class RouteConfig {

  @Autowired
  private AuthHandler authHandler;
  @Autowired
  private UserHandler userHandler;

  /**
   * authRoute.
   *
   * @return
   */
  @Bean
  public RouterFunction authRoute() {
    return RouterFunctions
        .route(POST(AUTH_URL_SIGNIN).and(accept(APPLICATION_JSON)), authHandler::signIn)
        .andRoute(POST(AUTH_URL_SIGNUP).and(accept(APPLICATION_JSON)), authHandler::signUp);
  }

  /**
   * userRoute.
   *
   * @return
   */
  @Bean
  public RouterFunction userRoute() {
    return RouterFunctions
        .route(GET(USER_URL_ME).and(accept(APPLICATION_JSON)), userHandler::getCurrentUser)
        .andRoute(GET(USER_URL_USER).and(accept(APPLICATION_JSON)), userHandler::user)
        .andRoute(GET(USER_URL_ADMIN).and(accept(APPLICATION_JSON)), userHandler::admin);
  }
}
