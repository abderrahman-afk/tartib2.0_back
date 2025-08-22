package com.solidwall.tartib.implementations;

import com.solidwall.tartib.dto.projectevaluation.request.CreateProjectEvaluationDto;
import com.solidwall.tartib.dto.projectevaluation.response.ProjectEvaluationResponseDto;
import com.solidwall.tartib.entities.PonderationEntity;

public interface ProjectEvaluationImplementation {
    ProjectEvaluationResponseDto create(CreateProjectEvaluationDto data);
    ProjectEvaluationResponseDto update(Long projectId, CreateProjectEvaluationDto data);
    ProjectEvaluationResponseDto getByProjectId(Long projectId);
    PonderationEntity checkForPonderation(Long evaluationId); 
    void delete(Long projectId);
    void validateAutoEvaluation(Long projectId);
      void validateFinalAutoEvaluation(Long projectId);
}