package com.solidwall.tartib.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evaluation_critiria")
@Entity
public class EvaluationCritiriaEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_component_id")
    @JsonIgnore
    private EvaluationComponentEntity evaluationComponent;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "evaluationCritiria", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<EvaluationIndicateurEntity> indicateurs;

    @Column(name = "name", length = 1500, nullable = false)
    private String name;
    
    @Column(name = "code", length = 1500, nullable = true)
    private String code;

    @Column(name = "description", length = 1500, nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
}
