package com.rednavis.api.controller;

import static com.rednavis.core.option.RestOption.AUTH_URL;
import static com.rednavis.core.option.RestOption.AUTH_URL_SIGNIN;

import com.rednavis.auth.service.auth.AuthService;
import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
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

  @PostMapping(AUTH_URL_SIGNIN)
  public Mono<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
    return authService.signIn(signInRequest);
  }

  @PostMapping("/signup")
  public Mono<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
    return authService.signUp(signUpRequest);
  }
}
