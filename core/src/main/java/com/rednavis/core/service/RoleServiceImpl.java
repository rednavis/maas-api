package com.rednavis.core.service;

import com.rednavis.core.entity.RoleEntity;
import com.rednavis.core.exception.AppException;
import com.rednavis.core.mapper.RoleMapper;
import com.rednavis.core.repository.RoleRepository;
import com.rednavis.shared.dto.user.Role;
import com.rednavis.shared.dto.user.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

  private static final RoleMapper ROLE_MAPPER = RoleMapper.ROLE_MAPPER;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role findByRoleName(RoleName roleName) {
    RoleEntity roleEntity = roleRepository.findByRoleName(roleName)
        .orElseThrow(() -> new AppException("User Role not set. Add default roles to database."));
    return ROLE_MAPPER.entityToDto(roleEntity);
  }
}
