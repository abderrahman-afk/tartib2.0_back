package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.rolePermission.CreateDto;
import com.solidwall.tartib.dto.rolePermission.UpdateDto;
import com.solidwall.tartib.entities.RolePermissionEntity;

public interface RolePermissionImplementation {

    List<RolePermissionEntity> findAll();

    RolePermissionEntity findOne(Map<String, String> data);

    RolePermissionEntity getOne(Long id);

    List<RolePermissionEntity> create(CreateDto data);

    List<RolePermissionEntity> update(Long id, UpdateDto data);

    void delete(Long id);

}
