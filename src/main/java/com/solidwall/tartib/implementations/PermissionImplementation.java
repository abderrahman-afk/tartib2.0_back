package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.permission.CreateDto;
import com.solidwall.tartib.dto.permission.UpdateDto;
import com.solidwall.tartib.entities.PermissionEntity;

public interface PermissionImplementation {

    List<PermissionEntity> findAll();

    PermissionEntity findOne(Map<String, String> data);

    PermissionEntity getOne(Long id);

    PermissionEntity create(CreateDto data);

    PermissionEntity update(Long id, UpdateDto data);

    void delete(Long id);

}
