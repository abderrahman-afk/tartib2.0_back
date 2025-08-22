package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import com.solidwall.tartib.enums.DerogationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "derogation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DerogationEntity extends AbstractBaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "project_identity_id", nullable = false)
    private ProjectIdentityEntity projectIdentity;
    
    @ManyToOne
    @JoinColumn(name = "admissibility_grid_id", nullable = false)
    private AdmissibilityGridEntity admissibilityGrid;
    

    
    @Column(name = "justification", columnDefinition = "TEXT")
    private String justification;
    
    @Column(name = "requested_by", nullable = true)
    private String requestedBy;
    
    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    private DerogationStatus status = DerogationStatus.PENDING;
    
    @Column(name = "decided_by")
    private String decidedBy;
}