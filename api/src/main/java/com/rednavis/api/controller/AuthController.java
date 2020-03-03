package com.rednavis.api.controller;

import static com.rednavis.api.config.SwaggerConfig.BEARER_AUTH;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_REFRESH_TOKEN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNUP;

import com.rednavis.auth.service.auth.AuthService;
import com.rednavis.core.service.CurrentUserService;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = AUTH_URL, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

  @Autowired
  private AuthService authService;
  @Autowired
  private CurrentUserService currentUserService;

  @PostMapping(AUTH_URL_SIGNIN)
  public Mono<ApiResponse<SignInResponse>> signIn(@RequestBody SignInRequest signInRequest) {
    return authService.signIn(signInRequest)
        .map(ApiResponse::createSuccessResponse);
  }

  @PostMapping(AUTH_URL_SIGNUP)
  public Mono<ApiResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest signUpRequest) {
    return authService.signUp(signUpRequest)
        .map(ApiResponse::createSuccessResponse);
  }

  @PostMapping(AUTH_URL_REFRESH_TOKEN)
  public Mono<ApiResponse<SignInResponse>> signUp(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    return authService.refreshToken(refreshTokenRequest)
        .map(ApiResponse::createSuccessResponse);
  }

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
        .map(ApiResponse::createSuccessResponse);
  }
}
