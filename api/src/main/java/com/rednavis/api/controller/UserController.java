package com.rednavis.api.controller;

import com.rednavis.auth.annotation.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private AuthService authService;

  @GetMapping("me")
  @PreAuthorize("hasRole('USER')")
  public com.rednavis.auth.security.CurrentUser getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    return authService.getCurrentUser(currentUser);
  }
}
