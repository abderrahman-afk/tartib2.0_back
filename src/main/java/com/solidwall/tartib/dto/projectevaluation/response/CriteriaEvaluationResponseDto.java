package com.solidwall.tartib.dto.projectevaluation.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaEvaluationResponseDto {
    private Long id;
    private Long criteriaId;
    private String criteriaName;
    private List<IndicatorEvaluationResponseDto> indicators;
}