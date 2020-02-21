package com.rednavis.auth.service;

import com.rednavis.auth.exception.ConflictException;
import com.rednavis.auth.mapper.CurrentUserMapper;
import com.rednavis.auth.security.CurrentUser;
import com.rednavis.auth.security.JwtTokenProvider;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.core.service.UserService;
import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import java.util.Set;
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

  private static final CurrentUserMapper CURRENT_USER_MAPPER = CurrentUserMapper.CURRENT_USER_MAPPER;

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserService userService;
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
    log.info("User with [email: {}] has logged in", userPrincipal.getUsername());
    return new JwtAuthenticationResponse(jwt);
  }

  @Override
  public String registerUser(SignUpRequest signUpRequest) {
    if (userService.existsByEmail(signUpRequest.getEmail())) {
      throw new ConflictException("Email [email: " + signUpRequest.getEmail() + "] is already taken");
    }

    User user = User.builder()
        .firstName(signUpRequest.getFirstName())
        .lastName(signUpRequest.getLastName())
        .email(signUpRequest.getEmail())
        .password(passwordEncoder.encode(signUpRequest.getPassword()))
        .roles(Set.of(RoleEnum.ROLE_USER))
        .build();

    log.info("Successfully registered user with [email: {}]", user.getEmail());
    return userService.save(user)
        .getId();
  }

  @Override
  public CurrentUser getCurrentUser(UserPrincipal userPrincipal) {
    User user = userService.findById(userPrincipal.getId());
    return CURRENT_USER_MAPPER.userToCurrentUser(user);
  }
}
