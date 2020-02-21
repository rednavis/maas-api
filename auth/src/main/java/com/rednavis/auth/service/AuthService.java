package com.rednavis.auth.service;

import com.rednavis.auth.security.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import reactor.core.publisher.Mono;

public interface AuthService {

  Mono<JwtAuthenticationResponse> authenticateUser(LoginRequest loginRequest);

  Mono<SignUpResponse> registerUser(SignUpRequest signUpRequest);

  Mono<CurrentUser> getCurrentUser(UserPrincipal userPrincipal);
}
