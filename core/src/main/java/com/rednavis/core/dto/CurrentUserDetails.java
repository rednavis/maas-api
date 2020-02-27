package com.rednavis.core.dto;

import com.rednavis.shared.security.CurrentUser;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CurrentUserDetails extends CurrentUser implements UserDetails {

  private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
  }

  //TODO LAV - Update user auth
  @Override
  public String getUsername() {
    return getId();
  }

  //TODO LAV - Update user auth
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  //TODO LAV - Update user auth
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  //TODO LAV - Update user auth
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  //TODO LAV - Update user auth
  @Override
  public boolean isEnabled() {
    return true;
  }
}
