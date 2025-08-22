package com.solidwall.tartib.entities;

import java.util.List;
import java.util.ArrayList;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "project_scale")
@Entity
public class ProjectScaleEntity extends AbstractBaseEntity {
    @ManyToOne
    @JoinColumn(name = "project_identity_id", nullable = true)
    private ProjectIdentityEntity projectIdentity;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "minimum_budget", nullable = false)
    private Long minimumBudget;

    @Column(name = "maximum_budget", nullable = false)
    private Long maximumBudget;

    @Column(name = "description", length = 255, nullable = true)
    private String description;
    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "project_scale_study", joinColumns = @JoinColumn(name = "project_scale_id"), inverseJoinColumns = @JoinColumn(name = "study_id"))
    private List<StudyEntity> studies = new ArrayList<>();
}
