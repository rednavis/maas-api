package com.rednavis.auth.security;

import com.rednavis.core.mapper.CurrentUserMapper;
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

@Slf4j
@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

  private static final CurrentUserMapper CURRENT_USER_MAPPER = CurrentUserMapper.CURRENT_USER_MAPPER;

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
        .flatMap(token -> userService.findById(token.getName()))
        .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
        .map(user -> createAuthentication(user, authentication));
  }

  private Authentication createAuthentication(User user, Authentication authentication) {
    UserDetails userDetails = CURRENT_USER_MAPPER.userToCurrentUserDetails(user);
    return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
  }

  private <T> Mono<T> raiseBadCredentials() {
    return Mono.error(new BadCredentialsException("Invalid Credentials"));
  }
}