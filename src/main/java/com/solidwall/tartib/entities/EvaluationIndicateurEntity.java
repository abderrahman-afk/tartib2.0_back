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
@Table(name = "evaluation_indicateur")
@Entity
public class EvaluationIndicateurEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_critiria_id")
    @JsonIgnore
    private EvaluationCritiriaEntity evaluationCritiria;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "evaluationIndicateur", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<EvaluationNormeEntity> normes;

    @Column(name = "name", length = 1500, nullable = false)
    private String name;

    @Column(name = "description", length = 1500, nullable = true, columnDefinition = "TEXT")
    private String description;
    @Column(name = "code", length = 1500, nullable = true)
    private String code;

    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
}
