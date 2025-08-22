package com.solidwall.tartib.dto.classementPortfolio;

import lombok.Data;
@Data
public class GeographicBonusDto {
    private Long geographicCategoryId;
    private Double bonusPercentage;
    private String comment;
}