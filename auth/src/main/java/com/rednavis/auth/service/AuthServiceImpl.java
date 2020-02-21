package com.rednavis.auth.service;

import com.rednavis.auth.mapper.CurrentUserMapper;
import com.rednavis.auth.security.CurrentUser;
import com.rednavis.auth.security.JwtTokenProvider;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.core.service.UserService;
import com.rednavis.shared.dto.auth.JwtAuthenticationResponse;
import com.rednavis.shared.dto.auth.LoginRequest;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  private static final CurrentUserMapper CURRENT_USER_MAPPER = CurrentUserMapper.CURRENT_USER_MAPPER;

  //@Autowired
  //private AuthenticationManager authenticationManager;
  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtTokenProvider tokenProvider;

  @Override
  public Mono<JwtAuthenticationResponse> authenticateUser(LoginRequest loginRequest) {
    //Authentication authentication = authenticationManager
    //    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    //SecurityContextHolder.getContext().setAuthentication(authentication);
    //String jwt = tokenProvider.generateToken(authentication);
    //UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    //log.info("User with [email: {}] has logged in", userPrincipal.getUsername());
    //return Mono.just(JwtAuthenticationResponse.builder()
    //    .accessToken(jwt)
    //    .build());
    return null;
  }

  @Override
  public Mono<SignUpResponse> registerUser(SignUpRequest signUpRequest) {
    //if (userService.existsByEmail(signUpRequest.getEmail())) {
    //  throw new ConflictException("Email [email: " + signUpRequest.getEmail() + "] is already taken");
    //}
    //
    //User user = User.builder()
    //    .firstName(signUpRequest.getFirstName())
    //    .lastName(signUpRequest.getLastName())
    //    .email(signUpRequest.getEmail())
    //    .password(passwordEncoder.encode(signUpRequest.getPassword()))
    //    .roles(Set.of(RoleEnum.ROLE_USER))
    //    .build();
    //
    //log.info("Successfully registered user with [email: {}]", user.getEmail());
    //return userService.save(user)
    //    .map(userMono -> userMono.getId())
    //    .map(id -> SignUpResponse.builder()
    //        .id(id)
    //        .build());
    return null;
  }

  @Override
  public Mono<CurrentUser> getCurrentUser(UserPrincipal userPrincipal) {
    return userService.findById(userPrincipal.getId())
        .map(user -> CURRENT_USER_MAPPER.userToCurrentUser(user));
  }
}
