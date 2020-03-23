package com.rednavis.auth.security;

import static com.rednavis.core.mapper.MapperProvider.CURRENT_USER_MAPPER;

import java.util.Collection;
import java.util.stream.Collectors;

import com.rednavis.database.service.UserService;
import com.rednavis.shared.dto.user.User;
import com.rednavis.shared.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

  private final UserService userService;

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
    CurrentUser currentUser = CURRENT_USER_MAPPER.userToCurrentUser(user);
    Collection<? extends GrantedAuthority> authorities = currentUser.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
    return new UsernamePasswordAuthenticationToken(currentUser, authentication.getCredentials(), authorities);
  }

  private <T> Mono<T> raiseBadCredentials() {
    return Mono.error(new BadCredentialsException("Invalid Credentials"));
  }
}