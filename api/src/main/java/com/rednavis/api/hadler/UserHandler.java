package com.rednavis.api.hadler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.rednavis.core.service.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

  @Autowired
  private CurrentUserService currentUserService;

  /**
   * getCurrentUser.
   *
   * @param request request
   * @return
   */
  public Mono<ServerResponse> getCurrentUser(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(currentUserService.getCurrentUser()));
  }

  /**
   * user.
   *
   * @param request request
   * @return
   */
  public Mono<ServerResponse> user(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue("Content for user"));
  }

  /**
   * admin.
   *
   * @param request request
   * @return
   */
  public Mono<ServerResponse> admin(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue("Content for admin"));
  }
}
