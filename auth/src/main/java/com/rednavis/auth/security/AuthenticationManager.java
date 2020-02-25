package com.rednavis.auth.security;

import com.rednavis.auth.jwt.JwtTokenProvider;
import com.rednavis.core.dto.CurrentUser;
import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @Autowired
  private UserService userService;

  @Override
  public Mono<Authentication> authenticate(final Authentication authentication) {
    if (authentication.isAuthenticated()) {
      return Mono.just(authentication);
    }
    return Mono.just(authentication)
        .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
        .cast(UsernamePasswordAuthenticationToken.class)
        .flatMap(this::authenticateToken)
        .publishOn(Schedulers.parallel())
        .onErrorResume(e -> raiseBadCredentials())
        .map(user -> createAuthentication(user, authentication));
  }

  private Authentication createAuthentication(User user, Authentication authentication) {
    UserDetails userDetails = CurrentUser.create(user);
    return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
  }

  private Mono<User> authenticateToken(final UsernamePasswordAuthenticationToken authenticationToken) {
    String authToken = authenticationToken.getCredentials()
        .toString();
    log.info("authToken: {}", authToken);
    if (!jwtTokenProvider.validateToken(authToken)) {
      return raiseBadCredentials();
    }
    String userId = jwtTokenProvider.getUserIdFromJwt(authToken);
    return userService.findById(userId);
  }

  private <T> Mono<T> raiseBadCredentials() {
    return Mono.error(new BadCredentialsException("Invalid Credentials"));
  }
}