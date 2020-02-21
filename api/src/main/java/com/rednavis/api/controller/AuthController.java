package com.rednavis.api.controller;

import static com.rednavis.core.option.RestOption.AUTH_URL;

import com.rednavis.auth.service.AuthService;
import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AUTH_URL)
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public Mono<JwtAuthenticationResponse> signIn(@RequestBody LoginRequest loginRequest) {
    return authService.authenticateUser(loginRequest);
  }

  @PostMapping("/signup")
  public Mono<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
    return authService.registerUser(signUpRequest);
  }
}
