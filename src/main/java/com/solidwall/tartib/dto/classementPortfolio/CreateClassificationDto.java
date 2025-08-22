package com.solidwall.tartib.dto.classementPortfolio;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateClassificationDto  {
    private String code;
    private String title;
    private String description;
    private String year;
    private MultipartFile justificationFile;
    private Long nomenclatureSecteurId;
    private Long nomenclatureGeographicId;
    private Long evaluationGridId;
    private String secteurBonuses; // JSON string of SecteurBonusDto
    private String geographicBonuses; // JSON string of GeographicBonusDto
}