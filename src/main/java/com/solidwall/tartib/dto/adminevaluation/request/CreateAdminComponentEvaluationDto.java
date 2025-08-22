package com.solidwall.tartib.dto.adminevaluation.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateAdminComponentEvaluationDto {
    private Long componentId;
    private Long originalComponentResponseId;
    private List<CreateAdminCriteriaEvaluationDto> criteria;
    private Double adminComponentScore;
    private Double originalComponentScore;
}
