package com.solidwall.tartib.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "financial_source")
public class FinancialSourceEntity extends AbstractBaseEntity {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectPlan_id", nullable = false, referencedColumnName = "id")
    private ProjectPlanEntity projectPlan;


    @Column(name = "bailleur", length = 255, nullable = true)
    private String bailleur ;

    @Column(name = "type", length = 255, nullable = true)
    private String type;

    @Column(name = "devise", length = 255, nullable = true)
    private String devise;

    @Column(name = "montant",  nullable = true)
    private Long montant;

    @Column(name = "taux_echange",   nullable = true)
    private Long tauxEchange;

    @Column(name = "monant_Dinars",   nullable = true)
    private Long montantDinars;

    @Column(name = "statut", length = 255, nullable = true)
    private String statut;

}
