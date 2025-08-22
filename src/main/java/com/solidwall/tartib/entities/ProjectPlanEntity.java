package com.solidwall.tartib.entities;


import java.util.*;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_plan")
@Entity
public class ProjectPlanEntity extends AbstractBaseEntity {

   @OneToOne
    @JoinColumn(name = "project_identity_id", nullable = true, unique = true)
  private ProjectIdentityEntity projectIdentity;
  
  @Column(name = "cout_totale", nullable = true)
  private Long coutTotale;

  @Column(name = "taux_echange", nullable = true)
  private Long tauxEchange;

  @Column(name = "cout_dinars", nullable = true)
  private Long coutDinars;

  @Column(name = "observation", nullable = true,length = 255)
  private String observation;

  @Column(name = "montant_annuel", nullable = true)
  private Long montantAnnuel;

@OneToMany(mappedBy = "projectPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FinancialSourceEntity> financialSource = new ArrayList<>();

    @OneToMany(mappedBy = "projectPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RubriqueEntity> rubriques = new ArrayList<>();

}
