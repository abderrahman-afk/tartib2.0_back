package com.solidwall.tartib.dto.adminevaluation.response;

import java.util.List;

import lombok.Data;

@Data
public class AdminComponentEvaluationResponseDto {
    private Long id;
    private Long componentId;
    private String componentName;
    private Double adminComponentScore;
    private Double originalComponentScore;
    private List<AdminCriteriaEvaluationResponseDto> criteria;
}