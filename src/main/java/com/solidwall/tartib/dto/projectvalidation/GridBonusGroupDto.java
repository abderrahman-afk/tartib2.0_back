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
public class GridBonusGroupDto {
    private String type; // "critere" or "indicateur"
    private String name; // nom du critère ou indicateur
    private List<BonusOptionDto> bonusOptions; // ABCDE levels
}