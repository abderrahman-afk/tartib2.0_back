package com.solidwall.tartib.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ponderation")
public class PonderationEntity extends AbstractBaseEntity {

    @Column(name = "max_indicateur", nullable = false)
    private Integer maxindicateur;

    @Column(name = "max_note", nullable = false)
    private Integer maxnote;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "evaluation_grid_id", unique = true, nullable = false)
    private EvaluationGridEntity evaluationGrid;

    @OneToMany(mappedBy = "ponderation", cascade = CascadeType.ALL)
    private List<PonderationComponentEntity> ponderationcomponents;
}