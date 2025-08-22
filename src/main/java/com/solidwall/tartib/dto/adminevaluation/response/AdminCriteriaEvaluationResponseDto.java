package com.solidwall.tartib.dto.adminevaluation.response;

import java.util.List;

import lombok.Data;

@Data
public class AdminCriteriaEvaluationResponseDto {
    private Long id;
    private Long criteriaId;
    private String criteriaName;
    private Double adminCriteriaScore;
    private Double originalCriteriaScore;
    private List<AdminIndicatorEvaluationResponseDto> indicators;
}