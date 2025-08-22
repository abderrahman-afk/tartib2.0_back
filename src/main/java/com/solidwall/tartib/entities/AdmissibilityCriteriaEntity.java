package com.solidwall.tartib.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admissibility_criteria")
@Entity
public class AdmissibilityCriteriaEntity extends AbstractBaseEntity {

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admissibility_grid_id", nullable = false, referencedColumnName = "id")
    private AdmissibilityGridEntity admissibiltyGrid;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "requirement_level", length = 255, nullable = true, columnDefinition = "TEXT")
    private String requirementLevel;

    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
}
