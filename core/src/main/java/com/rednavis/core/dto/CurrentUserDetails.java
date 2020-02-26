package com.rednavis.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import com.rednavis.shared.security.CurrentUser;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CurrentUserDetails extends CurrentUser implements UserDetails {

  @JsonIgnore
  private String password;

  public CurrentUserDetails(String id, String password, Set<RoleEnum> roles, String firstName, String lastName, String email) {
    super(id, roles, firstName, lastName, email);
  }

  /**
   * create.
   *
   * @param user user
   * @return
   */
  public static CurrentUserDetails create(User user) {
    return new CurrentUserDetails(
        user.getId(),
        user.getPassword(),
        user.getRoles(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail());
  }

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
