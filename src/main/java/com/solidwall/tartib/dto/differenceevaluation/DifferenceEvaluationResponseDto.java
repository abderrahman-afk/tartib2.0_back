package com.solidwall.tartib.dto.differenceevaluation;

import java.util.Date;

import lombok.Data;

@Data
public class DifferenceEvaluationResponseDto {
    private Long id;
    // Project evaluation details
    private Long projectEvaluationId;
    private String projectResponse;
    private String projectRemarks;
    
    // Admin evaluation details
    private Long adminEvaluationId;
    private String adminResponse;
    private String adminRemarks;
    
    // Indicator position details
    private String indicatorName;
    private String componentName;
    private String criteriaName;
    private Long componentId;
    private Long criteriaId;
    private Long indicatorId;
    
    private String status;
    private Date createdAt;
    private Date updatedAt;
}