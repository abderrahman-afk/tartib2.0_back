package com.solidwall.tartib.dto.classificationGeneration;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ProjectClassementResponseDto {
    private Long id;
    private Long projectIdentityId;
    private String projectCode;
    private String projectName;
    private Date projectCreationDate;
     private String ministry;
    private List<Long> districts;
    private Double initialScore;
    private Double bonusSecteur;
    private Double bonusCategorie;
    private Double bonusGrille;
    private Double scoreBonifie;
    private Integer rang;
}