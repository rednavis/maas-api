package com.rednavis.auth.service.auth;

import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import reactor.core.publisher.Mono;

public interface AuthService {

  Mono<SignInResponse> signIn(SignInRequest signInRequest);

  Mono<SignUpResponse> signUp(SignUpRequest signUpRequest);
}
