package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.evaluationcomponent.*; 
import com.solidwall.tartib.entities.EvaluationComponentEntity;

public interface EvaluationComponentImplementation {
    List<EvaluationComponentEntity> findAll();
    List<EvaluationComponentEntity> findByGridId(Long gridId);
    EvaluationComponentEntity getOne(Long id);
    EvaluationComponentEntity create(CreateDto data);
    void delete(Long id);
}