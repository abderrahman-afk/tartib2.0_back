package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.differenceevaluation.CreateDifferenceEvaluationDto;
import com.solidwall.tartib.dto.differenceevaluation.DifferenceEvaluationResponseDto;

public interface DifferenceEvaluationImplementation {
    // Create new difference entry
    DifferenceEvaluationResponseDto create(CreateDifferenceEvaluationDto data);
    
    // Update existing difference
    DifferenceEvaluationResponseDto update(Long id, CreateDifferenceEvaluationDto data);
    
    // Get differences for a project
    List<DifferenceEvaluationResponseDto> findByProjectEvaluation(Long projectEvaluationId);
    
    // Get differences for an admin evaluation
    List<DifferenceEvaluationResponseDto> findByAdminEvaluation(Long adminEvaluationId);
    
    // Update difference status
    DifferenceEvaluationResponseDto updateStatus(Long id, String status);
    
    // Recalculate differences for a project
    List<DifferenceEvaluationResponseDto> recalculateDifferences(Long projectId);
    
    // Delete difference
    void delete(Long id);
    List<Long> findProjectsWithDifferences();

}