package com.rednavis.auth.service.auth;

import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import reactor.core.publisher.Mono;

public interface AuthService {

  Mono<ApiResponse<SignInResponse>> signIn(SignInRequest signInRequest);

  Mono<ApiResponse<SignUpResponse>> signUp(SignUpRequest signUpRequest);
}
