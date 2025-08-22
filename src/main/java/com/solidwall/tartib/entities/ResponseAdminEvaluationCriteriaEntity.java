package com.solidwall.tartib.entities;

import java.util.List;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "response_admin_evaluation_criteria")
@Entity
public class ResponseAdminEvaluationCriteriaEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_admin_evaluation_component_id")
    private ResponseAdminEvaluationComponentEntity responseAdminEvaluationComponent;

    @ManyToOne
    @JoinColumn(name = "criteria_id")
    private EvaluationCritiriaEntity criteria;

    @OneToMany(mappedBy = "responseAdminEvaluationCriteria", cascade = CascadeType.ALL)
    private List<ResponseAdminEvaluationIndicatorEntity> indicators;

    // Original criteria reference
    @OneToOne
    @JoinColumn(name = "original_criteria_response_id")
    private ResponseEvaluationCriteriaEntity originalCriteriaResponse;

    // Criteria level admin score
    @Column(name = "admin_criteria_score")
    private Double adminCriteriaScore;

    // Original criteria score for reference
    @Column(name = "original_criteria_score")
    private Double originalCriteriaScore;
}