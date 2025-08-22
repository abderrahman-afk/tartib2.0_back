package com.solidwall.tartib.dto.project.plan;

import java.sql.Date;

import java.util.List;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

  private Long projectIdentity;
  private List<com.solidwall.tartib.dto.financialsource.CreateDto> financialSource;
  private List<com.solidwall.tartib.dto.rubrique.CreateDto> rubriques;


  private Long coutTotale;

  private Long tauxEchange;

  private Long coutDinars;

  private String observation;

  private Long montantAnnuel;
}
