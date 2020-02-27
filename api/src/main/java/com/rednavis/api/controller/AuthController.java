package com.rednavis.api.controller;

import static com.rednavis.api.config.SwaggerConfig.BEARER_AUTH;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNUP;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_TEST_GET;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_TEST_POST;
import static java.time.OffsetDateTime.now;

import com.rednavis.auth.service.auth.AuthService;
import com.rednavis.core.service.CurrentUserService;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.request.TestRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.rest.response.TestResponse;
import com.rednavis.shared.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
  public Mono<ApiResponse<SignInResponse>> signIn(@RequestBody SignInRequest signInRequest) {
    return authService.signIn(signInRequest);
  }

  @PostMapping(AUTH_URL_SIGNUP)
  public Mono<ApiResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest signUpRequest) {
    return authService.signUp(signUpRequest);
  }

  @Autowired
  private CurrentUserService currentUserService;

  /**
   * getCurrentUser.
   *
   * @return
   */
  @GetMapping(AUTH_URL_CURRENTUSER)
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(security = @SecurityRequirement(name = BEARER_AUTH))
  public Mono<ApiResponse<CurrentUser>> getCurrentUser() {
    return currentUserService.getCurrentUser()
        .map(currentUserDetails -> {
          CurrentUser currentUser = CurrentUser.builder()
              .id(currentUserDetails.getId())
              .email(currentUserDetails.getEmail())
              .firstName(currentUserDetails.getFirstName())
              .lastName(currentUserDetails.getLastName())
              .roles(currentUserDetails.getRoles())
              .build();
          return ApiResponse.createSuccessResponse(currentUser);
        });
  }

  /**
   * testPost.
   *
   * @param testRequest testRequest
   * @return
   */
  @PostMapping(AUTH_URL_TEST_POST)
  public Mono<ApiResponse<TestResponse>> testPost(@RequestBody TestRequest testRequest) {
    TestResponse testResponse = TestResponse.builder()
        .valueOutput(testRequest.getValueInput() + " " + DateTimeFormatter.ISO_DATE_TIME
            .withZone(ZoneOffset.UTC)
            .format(now().toInstant()))
        .build();
    ApiResponse<TestResponse> apiResponse = ApiResponse.createSuccessResponse(testResponse);
    return Mono.just(apiResponse);
  }

  /**
   * testGet.
   *
   * @return
   */
  @GetMapping(AUTH_URL_TEST_GET)
  public Mono<ApiResponse<TestResponse>> testGet() {
    TestResponse testResponse = TestResponse.builder()
        .valueOutput(DateTimeFormatter.ISO_DATE_TIME
            .withZone(ZoneOffset.UTC)
            .format(now().toInstant()))
        .build();
    ApiResponse<TestResponse> apiResponse = ApiResponse.createSuccessResponse(testResponse);
    return Mono.just(apiResponse);
  }
}
