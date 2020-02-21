package com.rednavis.api.controller;

import com.rednavis.auth.security.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.auth.service.UserAuthService;
import com.rednavis.shared.dto.auth.UserSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserAuthService userAuthService;

  @GetMapping("me")
  @PreAuthorize("hasRole('USER')")
  public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    return userAuthService.getCurrentUser(currentUser);
  }
}
