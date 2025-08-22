package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.role.RoleDto;
import com.solidwall.tartib.entities.RoleEntity;

public interface RoleImplementation {

  List<RoleDto> findAll();

  RoleDto findOne(String name);

  RoleDto getOne(Long id);

  RoleEntity create(RoleEntity data);

  RoleEntity update(Long id, RoleEntity data);

  void delete(Long id);

}
