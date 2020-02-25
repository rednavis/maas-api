package com.rednavis.api.controller;

import static com.rednavis.shared.RestUrl.AUTH_URL;
import static com.rednavis.shared.RestUrl.AUTH_URL_SIGNIN;
import static com.rednavis.shared.RestUrl.AUTH_URL_SIGNUP;
import static com.rednavis.shared.RestUrl.AUTH_URL_TEST_GET;
import static com.rednavis.shared.RestUrl.AUTH_URL_TEST_POST;
import static java.time.OffsetDateTime.now;

import com.rednavis.auth.service.auth.AuthService;
import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import com.rednavis.shared.dto.auth.TestRequest;
import com.rednavis.shared.dto.auth.TestResponse;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping(AUTH_URL_SIGNUP)
  public Mono<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
    return authService.signUp(signUpRequest);
  }

  /**
   * testPost.
   *
   * @param testRequest testRequest
   * @return
   */
  @PostMapping(AUTH_URL_TEST_POST)
  public Mono<TestResponse> testPost(@RequestBody TestRequest testRequest) {
    TestResponse testResponse = TestResponse.builder()
        .valueOutput(testRequest.getValueInput() + " " + DateTimeFormatter.ISO_DATE_TIME
            .withZone(ZoneOffset.UTC)
            .format(now().toInstant()))
        .build();
    return Mono.just(testResponse);
  }

  /**
   * testGet.
   *
   * @return
   */
  @GetMapping(AUTH_URL_TEST_GET)
  public Mono<TestResponse> testGet() {
    TestResponse testResponse = TestResponse.builder()
        .valueOutput(DateTimeFormatter.ISO_DATE_TIME
            .withZone(ZoneOffset.UTC)
            .format(now().toInstant()))
        .build();
    return Mono.just(testResponse);
  }
}
