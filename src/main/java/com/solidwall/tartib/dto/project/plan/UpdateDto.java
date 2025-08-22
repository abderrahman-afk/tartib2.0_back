package com.solidwall.tartib.dto.project.plan;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {
  private Long projectIdentity;

   private List<com.solidwall.tartib.dto.financialsource.UpdateDto> financialSource;
   private List<com.solidwall.tartib.dto.rubrique.UpdateDto> rubriques;


  private Long coutTotale;

  private Long tauxEchange;

  private Long coutDinars;

  private String observation;

  private Long montantAnnuel;
}
