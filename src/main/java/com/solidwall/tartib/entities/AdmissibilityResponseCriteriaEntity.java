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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admissibility_response_criteria")
@Entity
public class AdmissibilityResponseCriteriaEntity extends AbstractBaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "response_grid_id", nullable = false)
    private AdmissibilityResponseGridEntity responseGrid;

    @ManyToOne
    @JoinColumn(name = "criteria_id", nullable = false)
    private AdmissibilityCriteriaEntity criteria;

    @Column(name = "response", length = 20, nullable = false)
    private String response; // YES, NO, IN_PROGRESS

    @Column(name = "matches", nullable = false)
    private boolean matches;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
}