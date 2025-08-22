package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Date;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_evaluation_response")
@Entity
public class AdminEvaluationResponseEntity extends AbstractBaseEntity {

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectIdentityEntity project;

    @ManyToOne
    @JoinColumn(name = "grid_id", nullable = true)
    private EvaluationGridEntity evaluationGrid;

    @Column(name = "evaluation_date", nullable = false)
    private Date evaluationDate;

    @OneToMany(mappedBy = "adminEvaluationResponse", cascade = CascadeType.ALL)
    private List<ResponseAdminEvaluationComponentEntity> components;

    @Column(name = "isActive", nullable = true)
    private boolean isActive;

    // Reference to original auto-evaluation
    @OneToOne
    @JoinColumn(name = "original_evaluation_id", nullable = false)
    private ProjectEvaluationResponseEntity originalEvaluation;

    // Global admin score
    @Column(name = "admin_global_score")
    private Double adminGlobalScore;

    // Optional: Original global score for quick reference
    @Column(name = "original_global_score")
    private Double originalGlobalScore;
}