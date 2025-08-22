package com.solidwall.tartib.dto.adminevaluation.request;


import lombok.Data;
import java.util.List;
import java.util.Date;

@Data
public class CreateAdminEvaluationDto {
    private Long projectId;
    private Long gridId;
    private Long originalEvaluationId;
    private Date evaluationDate;
    private List<CreateAdminComponentEvaluationDto> components;
    private Double adminGlobalScore;
    private Double originalGlobalScore;
}