package com.rednavis.auth.security;

import static com.rednavis.shared.util.StringUtils.BEARER_SPACE;

import com.rednavis.auth.jwt.JwtTokenEnum;
import com.rednavis.auth.jwt.JwtTokenService;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtServerSecurityContextRepository implements ServerSecurityContextRepository {

  private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER_SPACE.length();
  private static final UnaryOperator<String> isolateBearerValue = authValue -> authValue.substring(BEARER_SPACE.length());

  private final JwtTokenService jwtTokenService;
  private final JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager;

  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
    return Mono.justOrEmpty(serverWebExchange)
        .map(jwtTokenService::extractAuthorization)
        .filter(Objects::nonNull)
        .filter(matchBearerLength)
        .map(isolateBearerValue)
        .filter(token -> !token.isEmpty())
        .map(token -> jwtTokenService.checkToken(JwtTokenEnum.JWT_ACCESS_TOKEN, token))
        .filter(Objects::nonNull)
        .map(jwtTokenService::createAuthentication)
        .filter(Objects::nonNull)
        .flatMap(jwtReactiveAuthenticationManager::authenticate)
        .map(SecurityContextImpl::new);
  }
}
