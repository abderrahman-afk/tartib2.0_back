package com.solidwall.tartib.dto.projectevaluation.request;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CreateCriteriaEvaluationDto {
    private Long criteriaId;
    private List<CreateIndicatorEvaluationDto> indicators;
}