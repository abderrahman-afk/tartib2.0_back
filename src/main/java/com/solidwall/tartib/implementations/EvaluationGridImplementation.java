package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.evaluationgrid.*;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;

public interface EvaluationGridImplementation {
  List<EvaluationGridEntity> findAll();

  EvaluationGridEntity getOne(Long id);
  EvaluationGridEntity getDefault();


  EvaluationGridEntity create(CreateDto data);

  EvaluationGridEntity update(Long id, UpdateDto data);

  EvaluationGridEntity updateGeberalSetting(Long id, UpdateDto data);

  EvaluationGridEntity updateMaxValues(Long id, Map<String, Integer> maxValues);

  void deleteComponent(Long gridId, Long componentId);

  void deleteCriteria(Long gridId, Long componentId, Long criteriaId);

  void deleteIndicateur(Long gridId, Long componentId, Long criteriaId, Long indicateurId);
List<EvaluationCritiriaEntity> getAllCriteriaByGridId(Long gridId);
    List<EvaluationIndicateurEntity> getAllIndicatorsByCriteriaId(Long criteriaId);
  void delete(Long id);

EvaluationGridEntity activateGrid(Long id, Boolean status);

List<EvaluationGridEntity> desactivateAll();
public EvaluationGridEntity duplicate(Long id);
}
