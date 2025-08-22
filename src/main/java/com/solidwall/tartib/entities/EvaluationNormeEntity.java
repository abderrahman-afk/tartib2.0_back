package com.solidwall.tartib.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evaluation_norme")
@Entity
public class EvaluationNormeEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evaluation_indicateur_id")
    @JsonIgnore
    private EvaluationIndicateurEntity evaluationIndicateur;

    @Column(name = "name", length = 1500, nullable = false)
    private String name;

    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
    @Column(name = "note", nullable = true)
    private int note;
}