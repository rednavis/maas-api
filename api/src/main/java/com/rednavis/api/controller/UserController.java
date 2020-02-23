package com.rednavis.api.controller;

import static com.rednavis.core.option.RestOption.USER_URL;

import com.rednavis.auth.annotation.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.auth.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(USER_URL)
public class UserController {

  @Autowired
  private AuthService authService;

  @GetMapping("me")
  @PreAuthorize("hasRole('USER')")
  @Operation(security = @SecurityRequirement(name = "bearerAuth"))
  public Mono<com.rednavis.auth.security.CurrentUser> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    return authService.getCurrentUser(currentUser);
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  @Operation(security = @SecurityRequirement(name = "bearerAuth"))
  public Mono<ResponseEntity<String>> user() {
    return Mono.just(ResponseEntity.ok("Content for user"));
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(security = @SecurityRequirement(name = "bearerAuth"))
  public Mono<ResponseEntity<String>> admin() {
    return Mono.just(ResponseEntity.ok("Content for admin"));
  }
}
