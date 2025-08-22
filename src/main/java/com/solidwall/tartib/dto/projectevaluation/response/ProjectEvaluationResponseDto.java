package com.solidwall.tartib.dto.projectevaluation.response;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectEvaluationResponseDto {
    private Long id;
    private Long projectId;
    private String projectName;  // Added for convenience
    private Long gridId;
    private Date evaluationDate;
    private boolean isActive;
    private List<ComponentEvaluationResponseDto> components;
}
