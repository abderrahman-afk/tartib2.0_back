package com.solidwall.tartib.dto.adminevaluation.response;

import lombok.Data;

@Data
public class AdminIndicatorEvaluationResponseDto {
    private Long id;
    private Long indicatorId;
    private String indicatorName;
    private Double adminScore;
    private Double originalScore;
    private Double adminWeightedScore;
    private String adminSelectedNorme;
    private String adminComment;
    private String originalComment;
    private String adminJustification;
    private String originalJustification;
    private Long referenceStudyId;
    private String referenceStudyName;
}