package com.solidwall.tartib.dto.nomenclatureSecteur;

import java.util.Date;
import java.util.List;

import com.solidwall.tartib.dto.nomenclatureSecteur.Secteur.SecteurResponseDto;

import lombok.Data;

@Data
public class NomenclatureSecteurResponseDto {
    private Long id;
    private String code;
    private String title;
    private String justificationPath;
    private Integer year;
    private Boolean active;
    private List<SecteurResponseDto> secteurs;
    private Date createdAt;
    private Date updatedAt;
}