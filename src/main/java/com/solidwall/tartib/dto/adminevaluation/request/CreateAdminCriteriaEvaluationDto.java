package com.solidwall.tartib.dto.adminevaluation.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateAdminCriteriaEvaluationDto {
    private Long criteriaId;
    private Long originalCriteriaResponseId;
    private List<CreateAdminIndicatorEvaluationDto> indicators;
    private Double adminCriteriaScore;
    private Double originalCriteriaScore;
}