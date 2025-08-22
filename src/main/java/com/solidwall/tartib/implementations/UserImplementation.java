package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.CreateUserDto;
import com.solidwall.tartib.entities.UserEntity;

public interface UserImplementation {

  UserEntity getOne(Long id);

  UserEntity findOne();

  List<UserEntity> findAll();

  void delete(Long id);

  UserEntity create(CreateUserDto  data);

  UserEntity update(Long id, CreateUserDto  data);
}
