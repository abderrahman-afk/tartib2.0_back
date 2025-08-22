package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.entities.PonderationCriteriaEntity;
import com.solidwall.tartib.dto.ponderationCritiria.CreateDto;
import com.solidwall.tartib.dto.ponderationCritiria.UpdateDto;


public interface PonderationCriteriaImplementation {
    List<PonderationCriteriaEntity> findAll();
    PonderationCriteriaEntity getOne(Long id);
    PonderationCriteriaEntity create(Long componentId, CreateDto data);
    PonderationCriteriaEntity update(Long id, UpdateDto data);
    void delete(Long id);
}
