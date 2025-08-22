package com.solidwall.tartib.dto.gridbonus;

import java.util.List;

import lombok.Data;

@Data
public class CreateGridBonusDto {
    private String code;
    private String name;
    private Long classificationId;
    private Long criteriaId;
    private Long indicatorId; // Optional
    private List<GridBonusLevelDto> levels;
}