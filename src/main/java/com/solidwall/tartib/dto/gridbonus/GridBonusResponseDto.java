package com.solidwall.tartib.dto.gridbonus;

import java.util.List;

import lombok.Data;

@Data
public class GridBonusResponseDto {
    private Long id;
    private String code;
    private String name;
    private String criteriaName;
    private String indicatorName; // Optional
    private List<GridBonusLevelDto> levels;
}
