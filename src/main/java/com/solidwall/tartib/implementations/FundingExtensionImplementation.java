package com.solidwall.tartib.implementations;

import com.solidwall.tartib.entities.FundingExtensionEntity;
import java.util.List;

public interface FundingExtensionImplementation {

  List<FundingExtensionEntity> findAll();

  FundingExtensionEntity findOne();

  FundingExtensionEntity getOne(Long id);

  FundingExtensionEntity create(FundingExtensionEntity data);

  FundingExtensionEntity update(Long id, FundingExtensionEntity data);

  void delete(Long id);
  
}
