package com.rednavis.auth.service.auth;

import static com.rednavis.core.mapper.MapperProvider.CURRENT_USER_MAPPER;
import static com.rednavis.database.mapper.MapperProvider.USER_MAPPER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rednavis.api.MaasApiApplication;
import com.rednavis.api.MaasApiApplicationTest;
import com.rednavis.auth.jwt.JwtTokenEnum;
import com.rednavis.auth.jwt.JwtTokenInfo;
import com.rednavis.auth.jwt.JwtTokenService;
import com.rednavis.database.entity.RefreshTokenEntity;
import com.rednavis.database.entity.UserEntity;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.User;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.security.CurrentUser;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RequiredArgsConstructor
class AuthServiceTest extends MaasApiApplicationTest {

  @Autowired
  private JwtTokenService jwtTokenService;
  @Autowired
  private AuthService authService;
  @Autowired
  private UserService userService;

  @Test
  void signInSuccess() {
    SignInRequest signInRequest = SignInRequest.builder()
        .email(MaasApiApplication.ADMIN_EMAIL)
        .password(MaasApiApplication.ADMIN_PASSWORD)
        .build();
    Mono<SignInResponse> signInResponseMono = authService.signIn(signInRequest);
    StepVerifier
        .create(signInResponseMono)
        .assertNext(signInResponse -> {
          assertNotNull(signInResponse.getAccessToken());
          assertNotNull(signInResponse.getRefreshToken());
        })
        .verifyComplete();
  }

  @Test
  void signUp() {
    assertTrue(true, "Not supported yet.");
  }

  @Test
  void refreshTokenSuccess() {
    Mono<SignInResponse> signInResponseMono = userService.findByEmail(MaasApiApplication.ADMIN_EMAIL)
        .flatMap(admin -> {
          long currentTime = Instant.now().toEpochMilli();
          CurrentUser currentUser = CURRENT_USER_MAPPER.userToCurrentUser(admin);
          JwtTokenInfo refreshToken = jwtTokenService.generateToken(JwtTokenEnum.JWT_REFRESH_TOKEN, currentUser, currentTime);
          return saveRefreshToken(admin, refreshToken);
        })
        .map(refreshTokenEntity -> RefreshTokenRequest.builder()
            .refreshToken(refreshTokenEntity.getRefreshToken())
            .build())
        .flatMap(refreshTokenEntity -> authService.refreshToken(refreshTokenEntity));
    StepVerifier
        .create(signInResponseMono)
        .assertNext(signInResponse -> {
          assertNotNull(signInResponse.getAccessToken());
          assertNotNull(signInResponse.getRefreshToken());
        })
        .verifyComplete();
  }

  private Mono<RefreshTokenEntity> saveRefreshToken(User user, JwtTokenInfo refreshToken) {
    UserEntity userEntity = USER_MAPPER.dtoToEntity(user);

    RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
    refreshTokenEntity.setRefreshToken(refreshToken.getToken());
    refreshTokenEntity.setExpiration(Instant.ofEpochMilli(refreshToken.getExpirationTime()));
    refreshTokenEntity.setUserId(userEntity.getId());
    return getRefreshTokenRepository().save(refreshTokenEntity);
  }
}