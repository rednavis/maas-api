package com.rednavis.api.controller;

import static com.rednavis.core.option.RestOption.USER_URL;

import com.rednavis.auth.annotation.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.auth.service.AuthService;
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
  private AuthService authService;

  @GetMapping("me")
  @PreAuthorize("hasRole('USER')")
  public Mono<com.rednavis.auth.security.CurrentUser> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    return authService.getCurrentUser(currentUser);
  }
}
