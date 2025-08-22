package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_evaluation_response")
@Entity
public class ProjectEvaluationResponseEntity extends AbstractBaseEntity {

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectIdentityEntity project;

    @ManyToOne
    @JoinColumn(name = "grid_id", nullable = true)
    private EvaluationGridEntity evaluationGrid;

    @Column(name = "evaluation_date", nullable = false)
    private Date evaluationDate;

    @OneToMany(mappedBy = "projectEvaluationResponse", cascade = CascadeType.ALL)
    private List<ResponseEvaluationComponentEntity> components;

    @Column(name = "isActive", nullable = true)
    private boolean isActive;
}
