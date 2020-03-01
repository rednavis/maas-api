package com.rednavis.auth.security;

import static com.rednavis.shared.util.StringUtils.BEARER_SPACE;

import com.rednavis.auth.jwt.JwtTokenService;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerSecurityContextRepository implements ServerSecurityContextRepository {

  private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER_SPACE.length();
  private static final UnaryOperator<String> isolateBearerValue = authValue -> authValue.substring(BEARER_SPACE.length());

  @Autowired
  private JwtTokenService jwtTokenService;
  @Autowired
  private JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager;

  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
    return Mono.justOrEmpty(serverWebExchange)
        .map(swe -> jwtTokenService.extractAuthorization(swe))
        .filter(Objects::nonNull)
        .filter(matchBearerLength)
        .map(isolateBearerValue)
        .filter(token -> !token.isEmpty())
        .map(token -> jwtTokenService.checkToken(token))
        .map(signedJWTMono -> jwtTokenService.createAuthentication(signedJWTMono))
        .flatMap(authentication -> jwtReactiveAuthenticationManager.authenticate(authentication))
        .map(SecurityContextImpl::new);
  }
}
