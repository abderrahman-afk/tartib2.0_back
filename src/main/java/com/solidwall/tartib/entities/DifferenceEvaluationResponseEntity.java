package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "difference_evaluation_response")
public class DifferenceEvaluationResponseEntity extends AbstractBaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "project_evaluation_id")
    private ProjectEvaluationResponseEntity projectEvaluation;
    
    @ManyToOne
    @JoinColumn(name = "admin_evaluation_id")  
    private AdminEvaluationResponseEntity adminEvaluation;
    
    @ManyToOne
    @JoinColumn(name = "indicator_id")
    private EvaluationIndicateurEntity indicator;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "project_remarks", columnDefinition = "TEXT",length = 1500)
    private String projectRemarks;
    
    @Column(name = "admin_remarks", columnDefinition = "TEXT",length = 1500) 
    private String adminRemarks;
    
    @Column(name = "project_response",length = 1500)
    private String projectResponse;
    
    @Column(name = "admin_response",length = 1500)
    private String adminResponse;
    
    @Column(name = "component_id")
    private Long componentId;
    
    @Column(name = "criteria_id")  
    private Long criteriaId;
    
    @Column(name = "indicator_id", insertable=false, updatable=false)
    private Long indicatorId;
}
