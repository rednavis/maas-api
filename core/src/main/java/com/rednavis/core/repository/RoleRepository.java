package com.rednavis.core.repository;

import com.rednavis.core.entity.RoleEntity;
import com.rednavis.shared.dto.user.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  Optional<RoleEntity> findByRoleName(RoleName roleName);
}