package com.rednavis.auth.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
class JwtConfiguration {

  @Value("${jwt.secretKey}")
  private byte[] jwtSecret;

  @Value("${jwt.expirationInSec}")
  private int expirationInSec;
}
