package com.rednavis.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationInMs}")
  private int jwtExpirationInMs;

  /**
   * generateToken.
   *
   * @param authentication authentication
   * @return
   */
  public String generateToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    byte[] apiKeySecretBytes = Encoders.BASE64.encode(jwtSecret.repeat(50)
        .getBytes()).getBytes();
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    return Jwts.builder()
        .setSubject(Long.toString(userPrincipal.getId()))
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(signingKey, signatureAlgorithm)
        .compact();
  }

  /**
   * getUserIdFromJwt.
   *
   * @param token token
   * @return
   */
  public long getUserIdFromJwt(String token) {
    JwtParser jwtParser = Jwts.parserBuilder()
        .setSigningKey(Encoders.BASE64.encode(jwtSecret.repeat(50)
            .getBytes()).getBytes())
        .build();
    Claims claims = jwtParser.parseClaimsJws(token)
        .getBody();
    return Long.parseLong(claims.getSubject());
  }

  /**
   * validateToken.
   *
   * @param authToken authToken
   * @return
   */
  public boolean validateToken(String authToken) {
    try {
      JwtParser jwtParser = Jwts.parserBuilder()
          .setSigningKey(Encoders.BASE64.encode(jwtSecret.repeat(50)
              .getBytes()).getBytes())
          .build();
      jwtParser.parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }
}
