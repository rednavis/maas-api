package com.rednavis.database.entity;

import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class AbstractEntity {

  @CreatedBy
  private String user;

  @CreatedDate
  private Instant createdDate;

  @LastModifiedBy
  private String lastModifiedUser;

  @LastModifiedDate
  private Instant lastModifiedDate;
}
