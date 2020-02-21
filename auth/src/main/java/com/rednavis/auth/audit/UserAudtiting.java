package com.rednavis.auth.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAudtiting implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    String auditor = SecurityContextHolder.getContext().getAuthentication().getName();
    return Optional.of(auditor);
  }

}