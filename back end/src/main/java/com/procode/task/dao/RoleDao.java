package com.procode.task.dao;

import com.procode.task.model.entity.RoleEntity;
import com.procode.task.model.enums.UserRole;

import java.util.Optional;

public interface RoleDao {
    void save(RoleEntity role);


    Optional<RoleEntity> findByRoleName(UserRole role);
}
