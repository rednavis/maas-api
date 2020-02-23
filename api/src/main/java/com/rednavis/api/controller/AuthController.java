package com.rednavis.api.controller;

import static com.rednavis.core.option.RestOption.AUTH_URL;

import com.rednavis.auth.security.JWTUtil;
import com.rednavis.auth.service.auth.AuthService;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private PasswordService passwordService;

  /**
   * login.
   *
   * @param ar ar
   * @return
   */
  @PostMapping("/login")
  public Mono<ResponseEntity<?>> login(@RequestBody SignInRequest ar) {
    return authService.findByUsername(ar.getEmail()).map((userDetails) -> {
      if (passwordService.encode(ar.getPassword()).equals(userDetails.getPassword())) {
        return ResponseEntity.ok(SignInResponse.builder()
            .accessToken(jwtUtil.generateToken(userDetails))
            .build());
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
    }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @PostMapping("/signin")
  public Mono<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
    return authService.authenticateUser(signInRequest);
  }

  @PostMapping("/signup")
  public Mono<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
    return authService.registerUser(signUpRequest);
  }
}
