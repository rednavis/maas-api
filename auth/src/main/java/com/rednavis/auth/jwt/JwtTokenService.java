package com.rednavis.auth.jwt;

import static java.time.Instant.now;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rednavis.core.exception.JwtException;
import com.rednavis.core.exception.JwtExpiredException;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.security.CurrentUser;
import java.text.ParseException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ServerWebExchange;

@Configuration
@RequiredArgsConstructor
public class JwtTokenService {

  private static final JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.HS256;

  private final JwtConfiguration jwtConfiguration;
  private final JwtSignerProvider jwtSignerProvider;

  /**
   * Create and sign a JWT object using information from the current authenticated principal.
   *
   * @param currentUser currentUser
   * @return String representing a valid token
   */
  public JwtTokenInfo generateToken(JwtTokenEnum jwtTokenEnum, CurrentUser currentUser, long currentTime) {
    JwtTokenInfo jwtTokenInfo = new JwtTokenInfo();

    switch (jwtTokenEnum) {
      case JWT_ACCESS_TOKEN:
        jwtTokenInfo.setTokenExpirationInSec(jwtConfiguration.getJwtAccessTokenExpirationInSec());
        jwtTokenInfo.setExpirationTime(currentTime + jwtTokenInfo.getTokenExpirationInSec() * 1000);
        jwtTokenInfo.setToken(generateAccessToken(currentUser, jwtTokenInfo.getExpirationTime()));
        break;
      case JWT_REFRESH_TOKEN:
        jwtTokenInfo.setTokenExpirationInSec(jwtConfiguration.getJwtRefreshTokenExpirationInSec());
        jwtTokenInfo.setExpirationTime(currentTime + jwtTokenInfo.getTokenExpirationInSec() * 1000);
        jwtTokenInfo.setToken(generateRefreshToken(currentUser, jwtTokenInfo.getExpirationTime()));
        break;
      default:
        break;
    }

    return jwtTokenInfo;
  }

  private String generateAccessToken(CurrentUser currentUser, long expirationTime) {
    //Prepare JWT with claims set
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(currentUser.getEmail())
        .expirationTime(new Date(expirationTime))
        .claim(CurrentUser.Fields.id, currentUser.getId())
        .claim(CurrentUser.Fields.firstName, currentUser.getFirstName())
        .claim(CurrentUser.Fields.lastName, currentUser.getLastName())
        .claim(CurrentUser.Fields.userName, currentUser.getUserName())
        .claim(CurrentUser.Fields.roles, currentUser.getRoles()
            .stream()
            .map(Enum::name)
            .collect(Collectors.joining(",")))
        .build();
    SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWS_ALGORITHM), claimsSet);
    //Apply the HMAC protection
    try {
      signedJwt.sign(jwtSignerProvider.getJwsSigner(JwtTokenEnum.JWT_ACCESS_TOKEN));
    } catch (JOSEException e) {
      throw new JwtException("Can't sign currentUser [currentUser: " + currentUser + "]");
    }
    return signedJwt.serialize();
  }

  private String generateRefreshToken(CurrentUser currentUser, long expirationTime) {
    //Prepare JWT with claims set
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(currentUser.getEmail())
        .expirationTime(new Date(expirationTime))
        .build();
    SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWS_ALGORITHM), claimsSet);
    //Apply the HMAC protection
    try {
      signedJwt.sign(jwtSignerProvider.getJwsSigner(JwtTokenEnum.JWT_REFRESH_TOKEN));
    } catch (JOSEException e) {
      throw new JwtException("Can't sign refresh token email [email: " + currentUser.getEmail() + "]");
    }
    return signedJwt.serialize();
  }

  /**
   * extractAuthorization.
   *
   * @param serverWebExchange serverWebExchange
   * @return
   */
  public String extractAuthorization(ServerWebExchange serverWebExchange) {
    return serverWebExchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION);
  }

  /**
   * checkToken.
   *
   * @param token token
   * @return
   */
  public SignedJWT checkToken(JwtTokenEnum jwtTokenEnum, String token) {
    // Parse the JWS and verify its RSA signature
    SignedJWT signedJwt;
    try {
      signedJwt = SignedJWT.parse(token);
      JWSVerifier verifier;
      switch (jwtTokenEnum) {
        case JWT_ACCESS_TOKEN:
          verifier = new MACVerifier(jwtConfiguration.getJwtAccessTokenSecret());
          break;
        case JWT_REFRESH_TOKEN:
          verifier = new MACVerifier(jwtConfiguration.getJwtRefreshTokenSecret());
          break;
        default:
          throw new JwtException("Unknown token type [jwtTokenEnum: " + jwtTokenEnum.name() + "]");
      }
      if (!signedJwt.verify(verifier)) {
        throw new JwtException("Can't verify token [token: " + token + "]");
      }
      if (checkExpiration(signedJwt)) {
        throw new JwtExpiredException("Current token is expired [token: " + token + "]");
      }
      return signedJwt;
    } catch (ParseException | JOSEException e) {
      throw new JwtException("Can't parse token [token: " + token + "]");
    }
  }

  /**
   * createAuthentication.
   *
   * @param signedJwt signedJwt
   * @return
   */
  public Authentication createAuthentication(SignedJWT signedJwt) {
    try {
      String subject = signedJwt.getJWTClaimsSet()
          .getSubject();
      String id = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.id);
      String firstName = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.firstName);
      String lastName = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.lastName);
      String userName = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.userName);
      String roleInLine = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.roles);
      Set<RoleEnum> roles = Stream.of(roleInLine.split(","))
          .map(RoleEnum::valueOf)
          .collect(Collectors.toSet());
      CurrentUser currentUser = new CurrentUser(id, firstName, lastName, userName, subject, roles);

      Collection<? extends GrantedAuthority> authorities = Stream.of(roleInLine.split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
      return new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
    } catch (ParseException e) {
      throw new JwtException("Can't parse signedJwt [signedJwt: " + signedJwt.serialize() + "]");
    }
  }

  /**
   * checkExpiration.
   *
   * @param signedJwt signedJwt
   */
  private boolean checkExpiration(SignedJWT signedJwt) {
    try {
      Date expiration = signedJwt.getJWTClaimsSet().getExpirationTime();
      Instant expirationInstant = expiration.toInstant();
      return expirationInstant.isBefore(now());
    } catch (ParseException e) {
      throw new JwtException("Can't parse signedJwt [signedJwt: " + signedJwt.serialize() + "]");
    }
  }
}
