package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.DistrictEntity;

public interface DistrictImplementation {

  List<DistrictEntity> findAll();

  DistrictEntity findOne();

  DistrictEntity getOne(Long id);

  DistrictEntity create(DistrictEntity data);

  DistrictEntity update(Long id, DistrictEntity data);

  void delete(Long id);
  String getNameById(Long id);
}
