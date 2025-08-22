package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.stakeholder.CreateDto;
import com.solidwall.tartib.dto.stakeholder.UpdateDto;

import com.solidwall.tartib.entities.StakeHolderEntity;

public interface StakeHolderImplementation {
    List<StakeHolderEntity> findAll();

    StakeHolderEntity findOne();

    StakeHolderEntity getOne(Long id);

    StakeHolderEntity create(CreateDto data);

    StakeHolderEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
