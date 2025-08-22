package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;
import com.solidwall.tartib.dto.funding.tranche.CreateDto;
import com.solidwall.tartib.dto.funding.tranche.UpdateDto;
import com.solidwall.tartib.entities.FundingTrancheEntity;

public interface FundingTrancheImplementation {

    List<FundingTrancheEntity> findAll();

    FundingTrancheEntity findOne(Map<String, String> data);

    FundingTrancheEntity getOne(Long id);

    FundingTrancheEntity create(CreateDto data);

    FundingTrancheEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
