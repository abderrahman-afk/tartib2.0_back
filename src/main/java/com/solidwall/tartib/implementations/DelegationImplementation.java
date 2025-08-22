package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.delegation.CreateDto;
import com.solidwall.tartib.dto.delegation.UpdateDto;
import com.solidwall.tartib.entities.DelegationEntity;

public interface DelegationImplementation {

  List<DelegationEntity> findAll();
  List<DelegationEntity> findByGovernorat(Long id);

  DelegationEntity findOne();

  DelegationEntity getOne(Long id);

  DelegationEntity create(CreateDto data);

  DelegationEntity update(Long id, UpdateDto data);

  void delete(Long id);

}
