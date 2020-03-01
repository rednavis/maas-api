package com.rednavis.auth.service.auth;

import static com.rednavis.core.mapper.MapperProvider.CURRENT_USER_MAPPER;
import static com.rednavis.database.mapper.MapperProvider.USER_MAPPER;

import com.nimbusds.jwt.SignedJWT;
import com.rednavis.auth.jwt.JwtTokenEnum;
import com.rednavis.auth.jwt.JwtTokenInfo;
import com.rednavis.auth.jwt.JwtTokenService;
import com.rednavis.auth.service.password.PasswordService;
import com.rednavis.core.exception.BadRequestException;
import com.rednavis.core.exception.ConflictException;
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
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

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
    return userService.existsByEmail(signUpRequest.getEmail())
        .filter(exist -> !exist)
        .switchIfEmpty(Mono.error(new ConflictException("Wrong email [email: " + signUpRequest.getEmail() + "] is already taken")))
        .then(userService.save(createNewUser(signUpRequest)))
        .map(user -> SignUpResponse.builder()
            .id(user.getId())
            .build());
  }

  @Override
  public Mono<SignInResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    return Mono.just(refreshTokenRequest.getRefreshToken())
        .doOnNext(token -> {
          SignedJWT signedJwt = jwtTokenService.checkToken(JwtTokenEnum.JWT_REFRESH_TOKEN, token);
          jwtTokenService.checkExpiration(signedJwt);
        })
        .flatMap(token -> refreshTokenRepository.findRefreshTokenEntityByRefreshToken(token))
        .filter(Objects::nonNull)
        .flatMap(refreshTokenEntity -> userService.findById(refreshTokenEntity.getId()))
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
            .refreshToken(refreshToken.getToken())
            .build());
  }

  private Mono<RefreshTokenEntity> saveRefreshToken(User user, JwtTokenInfo refreshToken) {
    UserEntity userEntity = USER_MAPPER.dtoToEntity(user);

    RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
    refreshTokenEntity.setRefreshToken(refreshToken.getToken());
    refreshTokenEntity.setExpiration(Instant.ofEpochMilli(refreshToken.getExpirationTime()));
    refreshTokenEntity.setUserEntity(userEntity);
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
