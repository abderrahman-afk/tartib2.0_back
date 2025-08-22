package com.solidwall.tartib.dto.projectevaluation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateIndicatorEvaluationDto {
    private Long indicatorId;
    private Double score;
    private Double wheightedScore;
    private String selectedNorme;
    private String comment;
    private String justification;
    private Long referenceStudyId;
}