package com.rednavis.api.dto;

import lombok.Data;

@Data
public class ProcessDefinitionDto {
  protected String id;
  protected String key;
  protected String category;
  protected String description;
  protected String name;
  protected int version;
  protected String resource;
  protected String deploymentId;
  protected String diagram;
  protected boolean suspended;
  protected String tenantId;
  protected String versionTag;
  protected Integer historyTimeToLive;
  protected boolean isStartableInTasklist;
}
