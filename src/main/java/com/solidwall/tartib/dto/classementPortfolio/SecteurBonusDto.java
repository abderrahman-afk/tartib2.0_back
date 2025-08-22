package com.solidwall.tartib.dto.classementPortfolio;

import lombok.Data;

@Data
public class SecteurBonusDto  {
    private Long secteurId;
    private Double bonusPercentage;
    private String comment;
}