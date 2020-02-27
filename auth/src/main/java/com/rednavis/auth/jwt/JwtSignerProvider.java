package com.rednavis.auth.jwt;

import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class JwtSignerProvider {

  @Getter
  private JWSSigner signer;

  /**
   * JwtSignerProvider.
   *
   * @param jwtConfiguration jwtConfiguration
   */
  @Autowired
  public JwtSignerProvider(JwtConfiguration jwtConfiguration) {
    JWSSigner init;
    try {
      init = new MACSigner(jwtConfiguration.getJwtSecret());
    } catch (KeyLengthException e) {
      init = null;
    }
    this.signer = init;
  }
}
