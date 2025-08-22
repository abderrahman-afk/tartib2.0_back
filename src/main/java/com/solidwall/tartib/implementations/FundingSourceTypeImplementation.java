package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.FundingSourceTypeEntity;

public interface FundingSourceTypeImplementation {

    List<FundingSourceTypeEntity> findAll();

    FundingSourceTypeEntity findOne();

    FundingSourceTypeEntity getOne(Long id);

    FundingSourceTypeEntity create(FundingSourceTypeEntity data);

    FundingSourceTypeEntity update(Long id, FundingSourceTypeEntity data);

    void delete(Long id);
}
