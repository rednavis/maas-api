package com.rednavis.auth.security;

import com.rednavis.core.service.UserService;
import com.rednavis.shared.dto.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) {
    User user = userService.findByEmail(email);
    return UserPrincipal.create(user);
  }

  public UserDetails loadUserById(String id) {
    User user = userService.findById(id);
    return UserPrincipal.create(user);
  }
}