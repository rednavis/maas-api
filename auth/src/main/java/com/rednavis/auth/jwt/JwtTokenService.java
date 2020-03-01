package com.rednavis.auth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.security.CurrentUser;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class JwtTokenService {

  @Autowired
  private JwtConfiguration jwtConfiguration;
  @Autowired
  private JwtSignerProvider jwtSignerProvider;

  /**
   * Create and sign a JWT object using information from the current authenticated principal.
   *
   * @param currentUser currentUser
   * @return String representing a valid token
   */
  public String generateToken(CurrentUser currentUser) {
    //Prepare JWT with claims set
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(currentUser.getEmail())
        .expirationTime(new Date(new Date().getTime() + jwtConfiguration.getExpirationInSec() * 1000))
        .claim(CurrentUser.Fields.id, currentUser.getId())
        .claim(CurrentUser.Fields.firstName, currentUser.getFirstName())
        .claim(CurrentUser.Fields.lastName, currentUser.getLastName())
        .claim(CurrentUser.Fields.roles, currentUser.getRoles()
            .stream()
            .map(Enum::name)
            .collect(Collectors.joining(",")))
        .build();
    SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    //Apply the HMAC protection
    try {
      signedJwt.sign(jwtSignerProvider.getSigner());
    } catch (JOSEException e) {
      log.error("Error sign currentUser [currentUser: {}]", currentUser, e);
      return null;
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
  public Mono<SignedJWT> checkToken(String token) {
    // Parse the JWS and verify its RSA signature
    SignedJWT signedJWT;
    try {
      signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(jwtConfiguration.getJwtSecret());
      if (signedJWT.verify(verifier)) {
        return Mono.just(signedJWT);
      } else {
        return Mono.empty();
      }
    } catch (ParseException | JOSEException e) {
      log.error("Error parse token [token: {}]", token, e);
      return Mono.empty();
    }
  }

  /**
   * createAuthentication.
   *
   * @param signedJwtMono signedJwtMono
   * @return
   */
  public Authentication createAuthentication(Mono<SignedJWT> signedJwtMono) {
    SignedJWT signedJwt = signedJwtMono.block();
    try {
      String subject = signedJwt.getJWTClaimsSet()
          .getSubject();
      String id = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.id);
      String firstName = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.firstName);
      String lastName = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.lastName);
      String roleInLine = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.roles);
      Set<RoleEnum> roles = Stream.of(roleInLine.split(","))
          .map(RoleEnum::valueOf)
          .collect(Collectors.toSet());
      CurrentUser currentUser = new CurrentUser(id, firstName, lastName, subject, roles);

      Collection<? extends GrantedAuthority> authorities = Stream.of(roleInLine.split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
      return new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
    } catch (ParseException e) {
      log.error("Error parse signedJwt [signedJwt: {}]", signedJwt, e);
      return null;
    }
  }
}
