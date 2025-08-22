package com.solidwall.tartib.implementations;


import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.classificationGeneration.GenerateClassificationDto;
import com.solidwall.tartib.dto.classificationGeneration.GeneratedClassificationResponseDto;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
 
public interface ClassificationGenerationImplementation {
    
    /**
     * Generate a new classification based on the provided parameters
     */
    GeneratedClassificationResponseDto generateClassification(GenerateClassificationDto data);
    
    /**
     * Get all generated classifications
     */
    List<GeneratedClassificationResponseDto> findAll(Map<String, String> filters);
    
    /**
     * Get a specific generated classification by ID
     */
    GeneratedClassificationResponseDto getOne(Long id);
    
    /**
     * Delete a generated classification
     */
    void delete(Long id);
    
    /**
     * Get all generated classifications for a specific classification system
     */
    List<GeneratedClassificationResponseDto> findByClassificationSystem(Long classificationSystemId);
 /**
     * Get the latest (most recent) generated classification for a specific classification system
     */
    GeneratedClassificationResponseDto findLatestByClassificationSystem(Long classificationSystemId);
    // extract districs from projects
     List<Long> extractDistrictsFromProject(ProjectIdentityEntity project);
}