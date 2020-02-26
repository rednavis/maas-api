package com.rednavis.api.controller;

import static com.rednavis.shared.RestUrlUtils.USER_URL;
import static com.rednavis.shared.RestUrlUtils.USER_URL_ADMIN;
import static com.rednavis.shared.RestUrlUtils.USER_URL_USER;

import com.rednavis.database.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(USER_URL)
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(USER_URL_USER)
  @PreAuthorize("hasRole('USER')")
  @Operation(security = @SecurityRequirement(name = "bearerAuth"))
  public Mono<String> user() {
    return userService.user();
  }

  @GetMapping(USER_URL_ADMIN)
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(security = @SecurityRequirement(name = "bearerAuth"))
  public Mono<String> admin() {
    return userService.admin();
  }
}