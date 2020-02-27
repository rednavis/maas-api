package com.rednavis.auth.service.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rednavis.api.MaasApiApplication;
import com.rednavis.api.MaasApiApplicationTest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AuthServiceTest extends MaasApiApplicationTest {

  @Autowired
  private AuthService authService;

  @Test
  void signInSuccess() {
    SignInRequest signInRequest = SignInRequest.builder()
        .email(MaasApiApplication.ADMIN_EMAIL)
        .password(MaasApiApplication.ADMIN_PASSWORD)
        .build();
    Mono<SignInResponse> signInResponseMono = authService.signIn(signInRequest);
    StepVerifier
        .create(signInResponseMono)
        .assertNext(signInResponse -> assertNotNull(signInResponse.getAccessToken()))
        .verifyComplete();
  }

  @Test
  void signUp() {
    assertTrue(true,"Not supported yet.");
  }
}