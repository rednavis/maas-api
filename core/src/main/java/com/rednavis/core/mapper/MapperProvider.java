package com.rednavis.core.mapper;

import lombok.experimental.UtilityClass;
import org.mapstruct.factory.Mappers;

@UtilityClass
public class MapperProvider {

  public static final CurrentUserMapper CURRENT_USER_MAPPER = Mappers.getMapper(CurrentUserMapper.class);
}
