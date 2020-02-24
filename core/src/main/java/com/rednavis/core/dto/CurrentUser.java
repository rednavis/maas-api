package com.rednavis.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class CurrentUser implements UserDetails {

  private String id;
  @JsonIgnore
  private String password;
  @JsonIgnore
  private Set<RoleEnum> roles;
  @JsonIgnore
  private String firstName;
  @JsonIgnore
  private String lastName;
  @JsonIgnore
  private String email;

  /**
   * create.
   *
   * @param user user
   * @return
   */
  public static CurrentUser create(User user) {
    return new CurrentUser(
        user.getId(),
        user.getPassword(),
        user.getRoles(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
  }

  //TODO LAV - Update user auth
  @Override
  public String getUsername() {
    return id;
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