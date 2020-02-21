package com.rednavis.auth.service;

import com.rednavis.auth.security.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;

public interface AuthService {

  JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);

  String registerUser(SignUpRequest signUpRequest);

  CurrentUser getCurrentUser(UserPrincipal userPrincipal);
}
