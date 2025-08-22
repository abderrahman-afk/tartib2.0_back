package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.evaluationnorme.CreateDto;
import com.solidwall.tartib.entities.EvaluationNormeEntity;

public interface EvaluationNormeImplementation {
    List<EvaluationNormeEntity> findAll();
    List<EvaluationNormeEntity> findByIndicateurId(Long indicateurId);
    EvaluationNormeEntity getOne(Long id);
    EvaluationNormeEntity create(CreateDto data);
    void delete(Long id);
}
