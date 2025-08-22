package com.solidwall.tartib.implementations;


import com.solidwall.tartib.dto.adminevaluation.request.CreateAdminEvaluationDto;
import com.solidwall.tartib.dto.adminevaluation.response.AdminEvaluationResponseDto;

public interface AdminEvaluationImplementation {
    
    // Create new admin evaluation
    AdminEvaluationResponseDto create(CreateAdminEvaluationDto data);
    
    // Update existing admin evaluation
    AdminEvaluationResponseDto update(Long projectId, CreateAdminEvaluationDto data);
    
    // Get admin evaluation by project ID
    AdminEvaluationResponseDto getByProjectId(Long projectId);
    AdminEvaluationResponseDto getById(Long projectId);
    void  validateFinalAdminEvaluation(Long projectId);
    // Delete admin evaluation
    void delete(Long projectId);
    // validate admin evaluation
    void validateAdminEvaluation(Long projectId);
}