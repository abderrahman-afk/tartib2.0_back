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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admissibility_response_grid")
@Entity
public class AdmissibilityResponseGridEntity extends AbstractBaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectIdentityEntity project;

    @ManyToOne
    @JoinColumn(name = "grid_id", nullable = false)
    private AdmissibilityGridEntity grid;

    @OneToMany(mappedBy = "responseGrid", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdmissibilityResponseCriteriaEntity> responses;

    @Column(name = "is_admissible", nullable = false)
    private boolean isAdmissible;

    @Column(name = "derogation_text", columnDefinition = "TEXT")
    private String derogationText;

    @Column(name = "status", length = 50)
    private String status; // PENDING, ACCEPTED, REJECTED
}
