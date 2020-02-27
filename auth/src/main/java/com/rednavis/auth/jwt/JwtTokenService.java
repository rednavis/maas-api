package com.rednavis.auth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class JwtTokenService {

  public static final String AUTHS = "auths";

  @Autowired
  private JwtConfiguration jwtConfiguration;
  @Autowired
  private JwtSignerProvider jwtSignerProvider;

  /**
   * Create and sign a JWT object using information from the current authenticated principal.
   *
   * @param userDetails userDetails
   * @return String representing a valid token
   */
  public String generateToken(UserDetails userDetails) {
    //Prepare JWT with claims set
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(userDetails.getUsername())
        .expirationTime(new Date(new Date().getTime() + jwtConfiguration.getExpirationInSec() * 1000))
        //TODO LAV Example user roles
        //.claim(AUTHS, currentUserDetails.getAuthorities()
        //    .stream()
        //    .map(auth -> auth.getAuthority())
        //    .collect(Collectors.joining(",")))
        .build();
    SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    //Apply the HMAC protection
    try {
      signedJwt.sign(jwtSignerProvider.getSigner());
    } catch (JOSEException e) {
      log.error("Error sign userDetails [userDetails: {}]", userDetails, e);
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
    try {
      return Mono.just(SignedJWT.parse(token));
    } catch (ParseException e) {
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
      //TODO LAV Example user roles
      //String auths = signedJwt.getJWTClaimsSet()
      //    .getStringClaim(AUTHS);
      //Collection<? extends GrantedAuthority> authorities = Stream.of(auths.split(","))
      //    .map(auth -> new SimpleGrantedAuthority(auth))
      //    .collect(Collectors.toList());
      //return new UsernamePasswordAuthenticationToken(subject, null, authorities);
      return new UsernamePasswordAuthenticationToken(subject, null);
    } catch (ParseException e) {
      log.error("Error parse signedJwt [signedJwt: {}]", signedJwt, e);
      return null;
    }
  }
}
