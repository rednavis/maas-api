package com.rednavis.api.controller;

import static com.rednavis.api.config.SwaggerConfig.BEARER_AUTH;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_ADMIN;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_FINDALL;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_USER;

import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(USER_URL)
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping(USER_URL_USER)
  @PreAuthorize("hasRole('USER')")
  @Operation(security = @SecurityRequirement(name = BEARER_AUTH))
  public Mono<String> user() {
    return userService.user();
  }

  @GetMapping(USER_URL_ADMIN)
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(security = @SecurityRequirement(name = BEARER_AUTH))
  public Mono<String> admin() {
    return userService.admin();
  }

  @GetMapping(USER_URL_FINDALL)
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(security = @SecurityRequirement(name = BEARER_AUTH))
  public Flux<User> findAll() {
    return userService.findAll();
  }
}