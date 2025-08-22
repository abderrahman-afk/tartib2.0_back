package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.deanship.CreateDto;
import com.solidwall.tartib.dto.deanship.UpdateDto;
import com.solidwall.tartib.entities.DeanshipEntity;

public interface DeanshipsImplementation {

  List<DeanshipEntity> findAll();
  List<DeanshipEntity> findByDelegation(Long id);


  DeanshipEntity findOne();

  DeanshipEntity getOne(Long id);

  DeanshipEntity create(CreateDto data);

  DeanshipEntity update(Long id, UpdateDto data);

  void delete(Long id);

}
