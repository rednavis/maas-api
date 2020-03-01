package com.rednavis.database.mapper;

import com.rednavis.database.entity.UserEntity;
import com.rednavis.shared.dto.user.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  UserEntity dtoToEntity(User user);

  User entityToDto(UserEntity userEntity);

  List<UserEntity> listDtoToListEntity(List<User> userList);

  List<User> listEntityToListDto(List<UserEntity> userEntityList);
}
