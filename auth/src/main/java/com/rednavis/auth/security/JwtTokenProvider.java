package com.rednavis.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  @Value("${jwt.secretKey}")
  private byte[] jwtSecret;

  @Value("${jwt.expirationInMs}")
  private int jwtExpirationInMs;

  /**
   * generateToken.
   *
   * @param userDetails userDetails
   * @return
   */
  public String generateToken(UserDetails userDetails) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    Key signingKey = new SecretKeySpec(jwtSecret, signatureAlgorithm.getJcaName());
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
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
  public String getUserIdFromJwt(String token) {
    JwtParser jwtParser = createJwtParser();
    return jwtParser.parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  /**
   * validateToken.
   *
   * @param authToken authToken
   * @return
   */
  public boolean validateToken(String authToken) {
    try {
      JwtParser jwtParser = createJwtParser();
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

  private JwtParser createJwtParser() {
    return Jwts.parserBuilder()
        .setSigningKey(jwtSecret)
        .build();
  }
}
