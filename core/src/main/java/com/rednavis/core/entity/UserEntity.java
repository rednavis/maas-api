package com.rednavis.core.entity;

import com.rednavis.shared.dto.user.RoleEnum;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class UserEntity extends AbstractEntity {

  @Id
  private String id;
  private String firstName;
  private String lastName;
  @Indexed(unique = true)
  private String email;
  private String password;
  private Set<RoleEnum> roles;
}