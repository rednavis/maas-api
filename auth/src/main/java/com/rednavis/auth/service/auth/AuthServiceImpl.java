package com.rednavis.auth.service.auth;

import com.rednavis.auth.mapper.CurrentUserMapper;
import com.rednavis.auth.security.CurrentUser;
import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.core.service.UserService;
import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import com.rednavis.shared.dto.user.RoleEnum;
import java.util.Set;
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
  //@Autowired
  //private JwtTokenProvider tokenProvider;

  //username:passwowrd -> user:user
  private final String userUsername = "user";// password: user

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

  // this is just an example, you can load the user from the database from the repository
  private final UserPrincipal user = new UserPrincipal("1",
      userUsername,
      "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=",
      Set.of(RoleEnum.ROLE_USER));
  //username:passwowrd -> admin:admin
  private final String adminUsername = "admin";// password: admin
  private final UserPrincipal admin = new UserPrincipal("2",
      adminUsername,
      "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=",
      Set.of(RoleEnum.ROLE_ADMIN));

  @Override
  public Mono<SignInResponse> authenticateUser(SignInRequest signInRequest) {
    //Authentication authentication = authenticationManager
    //    .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
    //SecurityContextHolder.getContext().setAuthentication(authentication);
    //String jwt = tokenProvider.generateToken(authentication);
    //UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    //log.info("User with [email: {}] has logged in", userPrincipal.getUsername());
    //return Mono.just(SignInResponse.builder()
    //    .accessToken(jwt)
    //    .build());
    return null;
  }

  /**
   * findByUsername.
   *
   * @param username username
   * @return
   */
  public Mono<UserPrincipal> findByUsername(String username) {
    if (username.equals(userUsername)) {
      return Mono.just(user);
    } else if (username.equals(adminUsername)) {
      return Mono.just(admin);
    } else {
      return Mono.empty();
    }
  }
}
