package com.solidwall.tartib.dto.projectevaluation.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentEvaluationResponseDto {
    private Long id;
    private Long componentId;
    private String componentName;
    private List<CriteriaEvaluationResponseDto> criteria;
}
