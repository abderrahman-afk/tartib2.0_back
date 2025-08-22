package com.solidwall.tartib.dto.projectvalidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectValidationDetailDto {
    private Long id;
    private String code;
    private String intitule;

    private String ministryName;
    private Double scoreGlobal;
    private Double scoreBonifie;
    private Integer rang;
    private String etat;
    private String cout;
    private String finInterieur;
    // Detailed project information
    // private String code;
    private String secteur;
    // private String gouvernorat;
    // private String categorie;
    // private String prioriteSecteur;
    // private String prioriteGouvernorat;
    // private String marqueurGenre;
    // private String marqueurClimat;
}
