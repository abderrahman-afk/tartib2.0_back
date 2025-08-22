package com.solidwall.tartib.dto.differenceevaluation;

import lombok.Data;

@Data
public class CreateDifferenceEvaluationDto {
    private Long projectEvaluationId;
    private Long adminEvaluationId;
    private Long indicatorId;
    private String status;
    private String projectRemarks;
    private String adminRemarks;
    private String projectResponse;
    private String adminResponse;
    private Long componentId;
    private Long criteriaId;
}