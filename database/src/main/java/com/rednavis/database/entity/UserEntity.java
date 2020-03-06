package com.rednavis.database.entity;

import com.rednavis.shared.dto.user.RoleEnum;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("user")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AbstractEntity {

  @Id
  private String id;
  private String firstName;
  private String lastName;
  @Indexed(unique = true)
  private String email;
  @Indexed(unique = true)
  private String userName;
  private String password;
  private Set<RoleEnum> roles;
}