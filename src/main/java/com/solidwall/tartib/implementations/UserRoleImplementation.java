package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.userrole.CreateDto;
import com.solidwall.tartib.dto.userrole.UpdateDto;
import com.solidwall.tartib.dto.userrole.UserRoleDto;
import com.solidwall.tartib.entities.UserRoleEntity;

public interface UserRoleImplementation {

  List<UserRoleDto> findAll();

  UserRoleDto getOne(Long id);

  UserRoleDto findOne();
  UserRoleDto getByUserId(Long id);

  UserRoleDto create(CreateDto data);

  UserRoleDto update(Long id, UpdateDto data);

  void delete(Long id);
  
}
