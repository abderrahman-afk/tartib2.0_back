package com.solidwall.tartib.dto.classementPortfolio;

import lombok.Data;

@Data
public class SecteurBonusResponseDto {
    private Long id;
    private Long secteurId;
    private String secteurTitle;
    private Double bonusPercentage;
    private String comment;
}