package com.rednavis.auth.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

  @Getter
  private String id;
  @JsonIgnore
  private String email;
  @JsonIgnore
  private String password;
  @Getter
  private Set<RoleEnum> roles;

  /**
   * UserPrincipal.
   *
   * @param id       id
   * @param email    email
   * @param password password
   * @param roles    roles
   */
  public UserPrincipal(String id, String email, String password, Set<RoleEnum> roles) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

  /**
   * create.
   *
   * @param user user
   * @return
   */
  public static UserPrincipal create(User user) {
    return new UserPrincipal(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        user.getRoles()
    );
  }

  //TODO LAV - Update user auth
  @Override
  public String getUsername() {
    return email;
  }

  //TODO LAV - Update user auth
  @Override
  public String getPassword() {
    return password;
  }

  //TODO LAV - Update user auth
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
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