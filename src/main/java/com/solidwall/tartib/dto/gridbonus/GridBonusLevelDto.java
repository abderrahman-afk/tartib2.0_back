package com.solidwall.tartib.dto.gridbonus;

import lombok.Data;

@Data
public class GridBonusLevelDto {
    private String levelName;
    private Double pointsThreshold;
    private Double bonusPercentage;
    private String comment;
}