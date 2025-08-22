package com.solidwall.tartib.entities;

import java.util.List;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "response_evaluation_criteria")
@Entity
public class ResponseEvaluationCriteriaEntity extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "component_evaluation_id", nullable = false)
    private ResponseEvaluationComponentEntity responseEvaluationComponent;

    @ManyToOne
    @JoinColumn(name = "criteria_id", nullable = true)
    private EvaluationCritiriaEntity criteria;

    @OneToMany(mappedBy = "responseEvaluationCriteria", cascade = CascadeType.ALL)
    private List<ResponseEvaluationIndicatorEntity> indicators;
}
