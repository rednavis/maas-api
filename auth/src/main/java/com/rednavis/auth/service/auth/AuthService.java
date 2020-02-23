package com.rednavis.auth.service.auth;

import com.rednavis.auth.security.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import reactor.core.publisher.Mono;

public interface AuthService {

  Mono<SignInResponse> authenticateUser(SignInRequest signInRequest);

  Mono<SignUpResponse> registerUser(SignUpRequest signUpRequest);

  Mono<CurrentUser> getCurrentUser(UserPrincipal userPrincipal);

  Mono<UserPrincipal> findByUsername(String username);
}
