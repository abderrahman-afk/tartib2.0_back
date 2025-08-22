package com.solidwall.tartib.dto.classementPortfolio;

import lombok.Data;

@Data
public class GeographicBonusResponseDto {
    private Long id;
    private Long geographicCategoryId;
    private String geographicCategoryTitle;
    private Double bonusPercentage;
    private String comment;
}