package com.rednavis.api.controller;

import com.rednavis.auth.service.AuthService;
import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public JwtAuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
    return authService.authenticateUser(loginRequest);
  }

  @PostMapping("/signup")
  public Long signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    return authService.registerUser(signUpRequest);
  }
}
