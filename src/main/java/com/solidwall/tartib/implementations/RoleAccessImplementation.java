package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.roleAccess.CreateDto;
import com.solidwall.tartib.dto.roleAccess.FindDto;
import com.solidwall.tartib.dto.roleAccess.UpdateDto;
import com.solidwall.tartib.entities.RoleAccessEntity;

public interface RoleAccessImplementation {

    List<RoleAccessEntity> findAll();

    RoleAccessEntity findOne(FindDto data);

    RoleAccessEntity getOne(Long id);

    List<RoleAccessEntity> create(CreateDto data);

    List<RoleAccessEntity> update(Long id, UpdateDto data);

    void delete(Long id);

}
