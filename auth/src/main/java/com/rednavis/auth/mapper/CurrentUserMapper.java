package com.rednavis.auth.mapper;

import com.rednavis.auth.security.CurrentUser;
import com.rednavis.shared.dto.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrentUserMapper {

  CurrentUserMapper CURRENT_USER_MAPPER = Mappers.getMapper(CurrentUserMapper.class);

  CurrentUser userToCurrentUser(User user);
}
