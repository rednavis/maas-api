package com.rednavis.auth.service.auth;

import com.rednavis.auth.jwt.JwtTokenProvider;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.dto.CurrentUser;
import com.rednavis.core.exception.BadRequestException;
import com.rednavis.core.exception.ConflictException;
import com.rednavis.core.exception.NotFoundException;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private PasswordService passwordService;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
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
        .filter(user -> passwordService.validatePassword(user.getPassword(), signInRequest.getPassword()))
        .switchIfEmpty(Mono.error(new BadRequestException("Wrong email or password")))
        .map(user -> {
          UserDetails userDetails = CurrentUser.create(user);
          String token = jwtTokenProvider.generateToken(userDetails);
          return SignInResponse.builder()
              .accessToken(token)
              .build();
        })
        .switchIfEmpty(Mono.error(new NotFoundException("User not found [email: " + signInRequest.getEmail() + "]")));
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
        .filter(exist -> exist == false)
        .switchIfEmpty(Mono.error(new ConflictException("Email [email: " + signUpRequest.getEmail() + "] is already taken")))
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
