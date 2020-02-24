package com.rednavis.auth.hadler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.rednavis.auth.security.JwtTokenProvider;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.dto.CurrentUser;
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
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AuthHandler {

  @Autowired
  private PasswordService passwordService;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @Autowired
  private UserService userService;

  /**
   * signIn.
   *
   * @param request request
   * @return
   */
  public Mono<ServerResponse> signIn(ServerRequest request) {
    final Mono<SignInRequest> signInRequestMono = request.bodyToMono(SignInRequest.class);
    return signInRequestMono.flatMap(
        signInRequest -> userService.findByEmail(signInRequest.getEmail())
            .flatMap(user -> {
              boolean validate = passwordService.validatePassword(user.getPassword(), signInRequest.getPassword());
              if (!validate) {
                return ServerResponse.badRequest()
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue("Wrong email or password"));
              }
              UserDetails userDetails = CurrentUser.create(user);
              String token = jwtTokenProvider.generateToken(userDetails);
              SignInResponse signInResponse = SignInResponse.builder()
                  .accessToken(token)
                  .build();
              return ServerResponse.ok()
                  .contentType(APPLICATION_JSON)
                  .body(BodyInserters.fromValue(signInResponse));
            })
            .switchIfEmpty(ServerResponse.badRequest()
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue("User not found [email: " + signInRequest.getEmail() + "]"))));
  }

  /**
   * signUp.
   *
   * @param request request
   * @return
   */
  public Mono<ServerResponse> signUp(ServerRequest request) {
    final Mono<SignUpRequest> signUpRequestMono = request.bodyToMono(SignUpRequest.class);
    return signUpRequestMono.flatMap(signUpRequest -> userService.existsByEmail(signUpRequest.getEmail())
        .flatMap(exist -> {
          if (!exist) {
            return ServerResponse.badRequest()
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue("Email [email: " + signUpRequest.getEmail() + "] is already taken"));
          }
          User user = User.builder()
              .firstName(signUpRequest.getFirstName())
              .lastName(signUpRequest.getLastName())
              .email(signUpRequest.getEmail())
              .password(passwordService.generatePassword(signUpRequest.getPassword()))
              .roles(Set.of(RoleEnum.ROLE_USER))
              .build();
          return userService.save(user)
              .flatMap(userSave -> {
                SignUpResponse signUpResponse = SignUpResponse.builder()
                    .id(userSave.getId())
                    .build();
                return ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue(signUpResponse));
              });
        }));
  }
}
