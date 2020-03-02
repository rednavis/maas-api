package com.rednavis.database.entity;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("refreshToken")
@EqualsAndHashCode(callSuper = true)
//TODO LAV - Create scheduler for delete expired tokens
public class RefreshTokenEntity extends AbstractEntity {

  @Id
  private String id;
  @Indexed(unique = true)
  private String refreshToken;
  private Instant expiration;
  private String userId;
}
