package com.rednavis.core.mapper;

import com.rednavis.core.dto.CurrentUserDetails;
import com.rednavis.shared.dto.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrentUserMapper {

  CurrentUserMapper CURRENT_USER_MAPPER = Mappers.getMapper(CurrentUserMapper.class);

  CurrentUserDetails userToCurrentUserDetails(User user);
}
