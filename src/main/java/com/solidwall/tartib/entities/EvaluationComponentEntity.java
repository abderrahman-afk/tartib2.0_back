package com.solidwall.tartib.entities;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "evaulation_component")
@Entity
public class EvaluationComponentEntity extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_grid_id")
    @JsonIgnore
    private EvaluationGridEntity evaluationGrid;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "evaluationComponent", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<EvaluationCritiriaEntity> critirias;

    @Column(name = "name", length = 1500, nullable = false)
    private String name;

    @Column(name = "description", length = 1500, nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
}
