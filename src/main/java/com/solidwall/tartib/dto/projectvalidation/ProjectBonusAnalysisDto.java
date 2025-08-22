package com.solidwall.tartib.dto.projectvalidation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBonusAnalysisDto {
    private List<BonusOptionDto> sectorialBonuses;
    private List<BonusOptionDto> geographicBonuses;
    private Double gridBonusApplied; // Changed to simple Double showing actual bonus applied
}