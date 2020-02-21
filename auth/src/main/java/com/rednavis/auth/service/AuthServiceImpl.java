package com.rednavis.auth.service;

import com.rednavis.auth.exception.ConflictException;
import com.rednavis.auth.security.JwtTokenProvider;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.core.service.RoleService;
import com.rednavis.core.service.UserService;
import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.user.Role;
import com.rednavis.shared.dto.user.RoleName;
import com.rednavis.shared.dto.user.User;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserService userService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtTokenProvider tokenProvider;

  @Override
  public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    log.info("User with [email: {}] has logged in", userPrincipal.getEmail());
    return new JwtAuthenticationResponse(jwt);
  }

  @Override
  public long registerUser(SignUpRequest signUpRequest) {
    if (userService.existsByEmail(signUpRequest.getEmail())) {
      throw new ConflictException("Email [email: " + signUpRequest.getEmail() + "] is already taken");
    }

    User user = User.builder()
        .email(signUpRequest.getEmail())
        .name(signUpRequest.getName())
        .password(signUpRequest.getPassword())
        .build();
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Role userRole = roleService.findByRoleName(RoleName.ROLE_USER);
    user.setRoles(Collections.singleton(userRole));

    log.info("Successfully registered user with [email: {}]", user.getEmail());
    return userService.save(user)
        .getId();
  }
}
