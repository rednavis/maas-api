package com.rednavis.core.service;

import com.rednavis.shared.dto.user.Role;
import com.rednavis.shared.dto.user.RoleName;

public interface RoleService {

  Role findByRoleName(RoleName roleName);
}
