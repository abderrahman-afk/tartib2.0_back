package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.FundingSourceEntity;

public interface FundingSourceImplementation {

    List<FundingSourceEntity> findAll();

    FundingSourceEntity findOne();

    FundingSourceEntity getOne(Long id);

    FundingSourceEntity create(FundingSourceEntity data);

    FundingSourceEntity update(Long id, FundingSourceEntity data);

    void delete(Long id);
}
