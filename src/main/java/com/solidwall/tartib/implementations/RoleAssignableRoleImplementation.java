package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.roleAssignableRole.CreateDto;
import com.solidwall.tartib.dto.roleAssignableRole.UpdateDto;
import com.solidwall.tartib.entities.RoleAssignableRoleEntity;

public interface RoleAssignableRoleImplementation {
      List<RoleAssignableRoleEntity> findAll();
    List<RoleAssignableRoleEntity> findByRoleId(Long roleId);
    List<Long> getAssignableRoleIds(Long roleId);
    List<RoleAssignableRoleEntity> create(CreateDto data);
    List<RoleAssignableRoleEntity> update(Long roleId, UpdateDto data);
    void delete(Long id);
    void deleteByRoleId(Long roleId);
    List<Long> getAssignableRoleIdsForCurrentUser();
}