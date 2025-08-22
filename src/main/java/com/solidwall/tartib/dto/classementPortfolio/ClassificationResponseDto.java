package com.solidwall.tartib.dto.classementPortfolio;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ClassificationResponseDto  {
    private Long id;
    private String code;
    private String title;
    private String description;
    private String year;
    private String justificationPath;
    private Boolean active;
    private Long nomenclatureSecteurId;
    private String nomenclatureSecteurTitle;
    private Long nomenclatureGeographicId;
    private String nomenclatureGeographicTitle;
    private Long evaluationGridId;
    private String evaluationGridTitle;
    private List<SecteurBonusResponseDto> secteurBonuses;
    private List<GeographicBonusResponseDto> geographicBonuses;
    private Date createdAt;
    private Date updatedAt;
}