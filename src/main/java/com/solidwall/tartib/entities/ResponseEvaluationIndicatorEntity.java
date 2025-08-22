package com.solidwall.tartib.entities;

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
@Table(name = "response_evaluation_indicator")
@Entity
public class ResponseEvaluationIndicatorEntity extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "criteria_evaluation_id", nullable = false)
    private ResponseEvaluationCriteriaEntity responseEvaluationCriteria;

    @ManyToOne
    @JoinColumn(name = "indicator_id", nullable = true)
    private EvaluationIndicateurEntity indicator;

    @Column(name = "score")
    private Double score;

    @Column(name = "score_poondere")
    private Double wheightedScore;
    
    @Column(name = "selected_norme", length = 1500)
    private String selectedNorme;

    @Column(name = "comment", columnDefinition = "TEXT",length = 1500)
    private String comment;

    @Column(name = "justification", columnDefinition = "TEXT",length = 1500)
    private String justification;

    @ManyToOne
    @JoinColumn(name = "reference_study_for_project_id")
    private StudyForProject referenceStudy;
}