package com.solidwall.tartib.entities;

import java.util.List;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import java.util.ArrayList;

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
@Table(name = "project_logic")
@Entity
public class ProjectLogicEntity extends AbstractBaseEntity {
      @OneToOne
    @JoinColumn(name = "project_identity_id", nullable = true, unique = true)
    private ProjectIdentityEntity projectIdentity;

    @OneToMany(mappedBy = "projectLogic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ComponentLogicEntity> componentLogics = new ArrayList<>();

       @Column(name = "general_objective", length = 255, nullable = true, columnDefinition = "TEXT")
    private String generalObjective;

    @Column(name = "specific_objective", length = 255, nullable = true, columnDefinition = "TEXT")
    private String specific_objective;

    @Column(name = "results", length = 255, nullable = true, columnDefinition = "TEXT")
    private String results;

    @Column(name = "document_cadre", length = 255, nullable = true, columnDefinition = "TEXT")
    private String documentCadre;

    @Column(name = "year_start", length = 255, nullable = true, columnDefinition = "TEXT")
    private String yearStart;

    @Column(name = "year_end", length = 255, nullable = true, columnDefinition = "TEXT")
    private String yearEnd;

    @Column(name = "document_plan_travail", length = 255, nullable = true, columnDefinition = "TEXT")
    private String documentPlanTravail;
}
