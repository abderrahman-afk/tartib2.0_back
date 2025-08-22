package com.solidwall.tartib.dto.adminevaluation.request;

import lombok.Data;

@Data
public class CreateAdminIndicatorEvaluationDto {
    private Long indicatorId;
    private Long originalIndicatorResponseId;
    private Double adminScore;
    private Double adminWeightedScore;
    private String adminSelectedNorme;
    private String adminComment;
    private String adminJustification;
    private Long referenceStudyId;
}