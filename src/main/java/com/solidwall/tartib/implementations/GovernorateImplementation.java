package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.governorate.CreateDto;
import com.solidwall.tartib.dto.governorate.UpdateDto;
import com.solidwall.tartib.entities.GovernorateEntity;

public interface GovernorateImplementation {

  List<GovernorateEntity> findAll(Map<String,String> data);

  GovernorateEntity findOne();

  GovernorateEntity getOne(Long id);
  List <GovernorateEntity> findByDistrict(Long id_district);

  GovernorateEntity create(CreateDto data);

  GovernorateEntity update(Long id, UpdateDto data);

  void delete(Long id);

}
