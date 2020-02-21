package com.rednavis.auth.security;

import com.rednavis.shared.dto.user.RoleEnum;
import java.util.Set;
import lombok.Getter;

@Getter
public class CurrentUser {

  private String firstName;
  private String lastName;
  private String email;
  private Set<RoleEnum> roles;
}
