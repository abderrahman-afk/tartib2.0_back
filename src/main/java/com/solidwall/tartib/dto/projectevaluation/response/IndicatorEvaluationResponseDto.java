package com.solidwall.tartib.dto.projectevaluation.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndicatorEvaluationResponseDto {
    private Long id;
    private Long indicatorId;
    private String indicatorName;
    private Double score;
    private Double wheightedScore;
    private String selectedNorme;
    private String comment;
    private String justification;
    private Long referenceStudyId;
    private String referenceStudyName;  // Added for convenience

}
