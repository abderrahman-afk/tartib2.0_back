package com.solidwall.tartib.entities;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import com.solidwall.tartib.enums.EvaluationGridState;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evaluation_grid")
@Entity
public class EvaluationGridEntity extends AbstractBaseEntity {
    @Column(name = "name", length = 1500, nullable = false)
    private String name;

    @Column(name = "description", length = 1500, nullable = true, columnDefinition = "TEXT")
    private String description;
    // added general setting

    @Column(name = "code", length = 255, nullable = true)
    private String code;

    @Column(name = "max_component", nullable = true)
    private int maxComponents;

    @Column(name = "max_crtieras", nullable = true)
    private int maxCrtieras;

    @Column(name = "max_indicator", nullable = true)
    private int maxIndicator;

    @Column(name = "max_normes", nullable = true)
    private int maxNormes;

    @Column(name = "max_note", nullable = true)
    private int maxNote;
    @Column(name = "state", nullable = true)
    @Enumerated(EnumType.STRING)
    private EvaluationGridState state = EvaluationGridState.brouillon;

    // finish general setting
    @Column(name = "isActive", nullable = true)
    private boolean isActive;
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "evaluationGrid", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EvaluationComponentEntity> components;
}
