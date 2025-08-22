package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "response_admin_evaluation_indicator")
@Entity
public class ResponseAdminEvaluationIndicatorEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_admin_evaluation_criteria_id")
    private ResponseAdminEvaluationCriteriaEntity responseAdminEvaluationCriteria;

    @ManyToOne
    @JoinColumn(name = "indicator_id")
    private EvaluationIndicateurEntity indicator;

    @Column(name = "admin_score")
    private Double adminScore;

    @Column(name="admin_weighted_score")
    private Double adminWeightedScore;

    @Column(name = "admin_selected_norme",length = 1500)
    private String adminSelectedNorme;

    @Column(name = "admin_comment", columnDefinition = "TEXT",length = 1500)
    private String adminComment;

    @Column(name = "admin_justification", columnDefinition = "TEXT",length = 1500)
    private String adminJustification;

    @ManyToOne
    @JoinColumn(name = "reference_study_for_project_id")
    private StudyForProject referenceStudy;

    // Original indicator response with all its data preserved
    @OneToOne
    @JoinColumn(name = "original_indicator_response_id")
    private ResponseEvaluationIndicatorEntity originalIndicatorResponse;
}