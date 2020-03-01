package com.rednavis.auth.service.auth;

import static com.rednavis.core.mapper.MapperProvider.CURRENT_USER_MAPPER;

import com.rednavis.auth.jwt.JwtTokenService;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.exception.BadRequestException;
import com.rednavis.core.exception.ConflictException;
import com.rednavis.core.exception.NotFoundException;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private PasswordService passwordService;
  @Autowired
  private JwtTokenService jwtTokenService;
  @Autowired
  private UserService userService;

  /**
   * signIn.
   *
   * @param signInRequest signInRequest
   * @return
   */
  @Override
  public Mono<SignInResponse> signIn(SignInRequest signInRequest) {
    return userService.findByEmail(signInRequest.getEmail())
        .switchIfEmpty(Mono.error(new NotFoundException("User not found [email: " + signInRequest.getEmail() + "]")))
        .filter(user -> passwordService.validatePassword(user.getPassword(), signInRequest.getPassword()))
        .switchIfEmpty(Mono.error(new BadRequestException("Wrong email or password")))
        .map(user -> {
          CurrentUser currentUser = CURRENT_USER_MAPPER.userToCurrentUser(user);
          String token = jwtTokenService.generateToken(currentUser);
          return SignInResponse.builder()
              .accessToken(token)
              .build();
        });
  }

  /**
   * signUp.
   *
   * @param signUpRequest signUpRequest
   * @return
   */
  @Override
  public Mono<SignUpResponse> signUp(SignUpRequest signUpRequest) {
    return userService.existsByEmail(signUpRequest.getEmail())
        .filter(exist -> !exist)
        .switchIfEmpty(Mono.error(new ConflictException("Wrong email [email: " + signUpRequest.getEmail() + "] is already taken")))
        .then(userService.save(createNewUser(signUpRequest)))
        .map(user -> SignUpResponse.builder()
            .id(user.getId())
            .build());
  }

  private User createNewUser(SignUpRequest signUpRequest) {
    return User.builder()
        .firstName(signUpRequest.getFirstName())
        .lastName(signUpRequest.getLastName())
        .email(signUpRequest.getEmail())
        .password(passwordService.generatePassword(signUpRequest.getPassword()))
        .roles(Set.of(RoleEnum.ROLE_USER))
        .build();
  }
}
