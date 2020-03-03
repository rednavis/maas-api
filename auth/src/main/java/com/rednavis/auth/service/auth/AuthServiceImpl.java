package com.rednavis.auth.service.auth;

import static com.rednavis.core.mapper.MapperProvider.CURRENT_USER_MAPPER;
import static com.rednavis.database.mapper.MapperProvider.USER_MAPPER;

import com.rednavis.auth.jwt.JwtTokenEnum;
import com.rednavis.auth.jwt.JwtTokenInfo;
import com.rednavis.auth.jwt.JwtTokenService;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.exception.BadRequestException;
import com.rednavis.core.exception.ConflictException;
import com.rednavis.core.exception.JwtException;
import com.rednavis.core.exception.NotFoundException;
import com.rednavis.database.entity.RefreshTokenEntity;
import com.rednavis.database.entity.UserEntity;
import com.rednavis.database.repository.RefreshTokenRepository;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordService passwordService;
  private final JwtTokenService jwtTokenService;
  private final UserService userService;
  private final RefreshTokenRepository refreshTokenRepository;

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
        .flatMap(this::generateTokens);
  }

  /**
   * signUp.
   *
   * @param signUpRequest signUpRequest
   * @return
   */
  @Override
  public Mono<SignUpResponse> signUp(SignUpRequest signUpRequest) {
    return userService.findByEmail(signUpRequest.getEmail())
        .switchIfEmpty(Mono.error(new ConflictException("Wrong email [email: " + signUpRequest.getEmail() + "] is already taken")))
        .then(userService.save(createNewUser(signUpRequest)))
        .map(user -> SignUpResponse.builder()
            .id(user.getId())
            .build());
  }

  @Override
  public Mono<SignInResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    return Mono.just(refreshTokenRequest.getRefreshToken())
        .flatMap(token -> Mono.defer(() -> Mono.just(jwtTokenService.checkToken(JwtTokenEnum.JWT_REFRESH_TOKEN, token))
            .map(signedJwt -> jwtTokenService.checkExpiration(signedJwt))
            .filter(expire -> !expire)
            //TODO LAV How get signedJwt ?
            //.switchIfEmpty(Mono.error(new JwtException("Current token is expired [token: " + signedJwt.serialize() + "]")))
            .switchIfEmpty(Mono.error(new JwtException("Current token is expired [token: ]")))
            .thenReturn(token)))
        .flatMap(token -> refreshTokenRepository.findRefreshTokenEntityByRefreshToken(token))
        .filter(Objects::nonNull)
        .flatMap(refreshTokenEntity -> refreshTokenRepository.deleteById(refreshTokenEntity.getId())
            .then(userService.findById(refreshTokenEntity.getUserId())))
        .flatMap(this::generateTokens);
  }

  private Mono<SignInResponse> generateTokens(User user) {
    CurrentUser currentUser = CURRENT_USER_MAPPER.userToCurrentUser(user);

    long currentTime = Instant.now().toEpochMilli();
    JwtTokenInfo accessToken = jwtTokenService.generateToken(JwtTokenEnum.JWT_ACCESS_TOKEN, currentUser, currentTime);
    JwtTokenInfo refreshToken = jwtTokenService.generateToken(JwtTokenEnum.JWT_REFRESH_TOKEN, currentUser, currentTime);

    return saveRefreshToken(user, refreshToken)
        .thenReturn(SignInResponse.builder()
            .accessToken(accessToken.getToken())
            .accessTokenExpiration(accessToken.getTokenExpiration())
            .refreshToken(refreshToken.getToken())
            .refreshTokenExpiration(refreshToken.getTokenExpiration())
            .build());
  }

  private Mono<RefreshTokenEntity> saveRefreshToken(User user, JwtTokenInfo refreshToken) {
    UserEntity userEntity = USER_MAPPER.dtoToEntity(user);

    RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
    refreshTokenEntity.setRefreshToken(refreshToken.getToken());
    refreshTokenEntity.setExpiration(Instant.ofEpochMilli(refreshToken.getExpirationTime()));
    refreshTokenEntity.setUserId(userEntity.getId());
    return refreshTokenRepository.save(refreshTokenEntity);
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
