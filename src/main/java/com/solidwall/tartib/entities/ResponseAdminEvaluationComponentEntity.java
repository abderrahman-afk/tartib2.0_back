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
@Table(name = "response_admin_evaluation_component")
@Entity
public class ResponseAdminEvaluationComponentEntity extends AbstractBaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_evaluation_response_id")
    private AdminEvaluationResponseEntity adminEvaluationResponse;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private EvaluationComponentEntity component;

    @OneToMany(mappedBy = "responseAdminEvaluationComponent", cascade = CascadeType.ALL)
    private List<ResponseAdminEvaluationCriteriaEntity> criteria;

    // Original component reference
    @OneToOne
    @JoinColumn(name = "original_component_response_id")
    private ResponseEvaluationComponentEntity originalComponentResponse;

    // Component level admin score
    @Column(name = "admin_component_score")
    private Double adminComponentScore;

    // Original component score for reference
    @Column(name = "original_component_score")
    private Double originalComponentScore;
}
