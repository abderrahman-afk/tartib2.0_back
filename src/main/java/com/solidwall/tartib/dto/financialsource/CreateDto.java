package com.solidwall.tartib.dto.financialsource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDto {
    private Long projectPlan_id;

    private String bailleur;

    private String type;

    private String devise;

    private Long montant;

    private Long tauxEchange;

    private Long montantDinars;

    private String statut;

}
