package com.rednavis.auth.jwt;

import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.rednavis.core.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class JwtSignerProvider {

  private final JWSSigner signerAccessToken;
  private final JWSSigner signerRefreshToken;

  /**
   * JwtSignerProvider.
   *
   * @param jwtConfiguration jwtConfiguration
   */
  public JwtSignerProvider(JwtConfiguration jwtConfiguration) {
    JWSSigner initAccessToken;
    JWSSigner initRefreshToken;
    try {
      initAccessToken = new MACSigner(jwtConfiguration.getJwtAccessTokenSecret());
      initRefreshToken = new MACSigner(jwtConfiguration.getJwtRefreshTokenSecret());
    } catch (KeyLengthException e) {
      log.error("Error parse key secret length", e);
      initAccessToken = null;
      initRefreshToken = null;
    }
    signerAccessToken = initAccessToken;
    signerRefreshToken = initRefreshToken;
  }

  public JWSSigner getJwsSigner(JwtTokenEnum jwtTokenEnum) {
    switch (jwtTokenEnum) {
      case JWT_ACCESS_TOKEN:
        return signerAccessToken;
      case JWT_REFRESH_TOKEN:
        return signerRefreshToken;
      default:
        throw new JwtException("Unknown token type [jwtTokenEnum: " + jwtTokenEnum.name() + "]");
    }
  }
}
