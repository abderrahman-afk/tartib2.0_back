package com.solidwall.tartib.dto.adminevaluation.response;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AdminEvaluationResponseDto {
    private Long id;
    private Long projectId;
    private String projectName;
    private Long gridId;
    private Date evaluationDate;
    private boolean isActive;
    private Double adminGlobalScore;
    private Double originalGlobalScore;
    private List<AdminComponentEvaluationResponseDto> components;
}
