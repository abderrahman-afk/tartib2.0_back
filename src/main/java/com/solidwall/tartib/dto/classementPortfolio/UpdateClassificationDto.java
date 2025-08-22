package com.solidwall.tartib.dto.classementPortfolio;


import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateClassificationDto {
    private String code;
    private String title;
    private String description;
    private String year;
    private MultipartFile justificationFile;
    private Long nomenclatureSecteurId;
    private Long nomenclatureGeographicId;
    private Long evaluationGridId;
    private String secteurBonuses;
    private String geographicBonuses;
}