package com.rednavis.auth.service.auth;

import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import reactor.core.publisher.Mono;

public interface AuthService {

  Mono<SignInResponse> signIn(SignInRequest signInRequest);

  Mono<SignUpResponse> signUp(SignUpRequest signUpRequest);
}
