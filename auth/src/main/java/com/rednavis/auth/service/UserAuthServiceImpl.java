package com.rednavis.auth.service;

import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.shared.dto.auth.UserSummary;
import org.springframework.stereotype.Service;

@Service
public class UserAuthServiceImpl implements UserAuthService {

  @Override
  public UserSummary getCurrentUser(UserPrincipal userPrincipal) {
    return UserSummary.builder()
        .id(userPrincipal.getId())
        .email(userPrincipal.getEmail())
        .name(userPrincipal.getName())
        .build();
  }
}
