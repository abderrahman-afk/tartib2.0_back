package com.solidwall.tartib.dto.nomenclatureSecteur.Secteur;
import java.util.Set;

import com.solidwall.tartib.entities.MinisterEntity;

import lombok.Data;
@Data
public class SecteurResponseDto {
    private Long id;
    private String code;
    private String title;
    private String description;
    private Set<MinisterEntity> ministers;
}