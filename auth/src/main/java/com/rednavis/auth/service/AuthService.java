package com.rednavis.auth.service;

import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;

public interface AuthService {

  JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);

  long registerUser(SignUpRequest signUpRequest);
}
