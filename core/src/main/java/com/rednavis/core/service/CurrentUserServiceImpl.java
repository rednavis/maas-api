package com.rednavis.core.service;

import com.rednavis.core.dto.CurrentUserDetails;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

  @Override
  public Mono<CurrentUserDetails> getCurrentUser() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(Objects::nonNull)
        .map(Authentication::getPrincipal)
        .cast(CurrentUserDetails.class);
  }
}
