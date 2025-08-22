package com.solidwall.tartib.dto.projectevaluation.request;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectEvaluationDto {
    private Long projectId;
    private Long gridId;
    private Date evaluationDate;
    private List<CreateComponentEvaluationDto> components;
}