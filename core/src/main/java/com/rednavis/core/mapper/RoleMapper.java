package com.rednavis.core.mapper;

import com.rednavis.core.entity.RoleEntity;
import com.rednavis.shared.dto.user.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

  RoleMapper ROLE_MAPPER = Mappers.getMapper(RoleMapper.class);

  RoleEntity dtoToEntity(Role role);

  Role entityToDto(RoleEntity roleEntity);

  List<RoleEntity> listDtoToListEntity(List<Role> roleList);

  List<Role> listEntityToListDto(List<RoleEntity> roleEntityList);
}
