package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.evaluationindicateur.CreateDto;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;

public interface EvaluationIndicateurImplementation {
    List<EvaluationIndicateurEntity> findAll();
    List<EvaluationIndicateurEntity> findByCritiriaId(Long critiriaId);
    EvaluationIndicateurEntity getOne(Long id);
    EvaluationIndicateurEntity create(CreateDto data);
    void delete(Long id);
}