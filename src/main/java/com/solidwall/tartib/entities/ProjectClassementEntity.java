package com.solidwall.tartib.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_classement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectClassementEntity extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "generated_classification_id", nullable = false)
    private GeneratedClassificationEntity generatedClassification;

    @ManyToOne
    @JoinColumn(name = "project_identity_id", nullable = false)
    private ProjectIdentityEntity projectIdentity;
    
   @Column(name = "project_creation_date")
    private Date projectCreationDate;

    @Column(name = "initial_score")
    private Double initialScore;

    @Column(name = "bonus_secteur")
    private Double bonusSecteur;

    @Column(name = "bonus_categorie")
    private Double bonusCategorie;

    @Column(name = "bonus_grille")
    private Double bonusGrille;

    @Column(name = "score_bonifie")
    private Double scoreBonifie;

    @Column(name = "rang")
    private Integer rang;

     
    // @Column(name = "ministry", length = 255)
    // private String ministry;
    
    // @ElementCollection
    // @CollectionTable(name = "project_classement_districts", 
    //                  joinColumns = @JoinColumn(name = "project_classement_id"))
    // @Column(name = "district_id")
    // private List<Long> districts = new ArrayList<>();
}