package com.solidwall.tartib.dto.projectvalidation;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectValidationDataDto {
    private ProjectValidationDetailDto project;
    private BigDecimal coutTotal;
    private BigDecimal financementInterne;
    private ProjectScoreDataDto scoreData;
    private ProjectBonusAnalysisDto bonusAnalysis;
}