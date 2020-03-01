package com.rednavis.auth.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
class JwtConfiguration {

  @Value("${jwt.accessToken.secretKey}")
  private String jwtAccessTokenSecret;
  @Value("${jwt.accessToken.expirationInSec}")
  private int jwtAccessTokenExpirationInSec;
  @Value("${jwt.refreshToken.secretKey}")
  private String jwtRefreshTokenSecret;
  @Value("${jwt.refreshToken.expirationInSec}")
  private int jwtRefreshTokenExpirationInSec;
}
