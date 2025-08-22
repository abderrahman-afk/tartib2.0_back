package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.evaluationcritiria.CreateDto;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;

public interface EvaluationCritiriaImplementation {
    List<EvaluationCritiriaEntity> findAll();
    List<EvaluationCritiriaEntity> findByComponentId(Long componentId);
    EvaluationCritiriaEntity getOne(Long id);
    EvaluationCritiriaEntity create(CreateDto data);
    void delete(Long id);
}