package com.rednavis.database.mapper;

import lombok.experimental.UtilityClass;
import org.mapstruct.factory.Mappers;

@UtilityClass
public class MapperProvider {

  public static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
}
