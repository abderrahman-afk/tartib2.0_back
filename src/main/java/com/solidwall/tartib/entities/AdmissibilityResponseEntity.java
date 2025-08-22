package com.solidwall.tartib.entities;

import java.util.Date;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admissibility_response")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdmissibilityResponseEntity extends AbstractBaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "project_identity_id", nullable = false)
    private ProjectIdentityEntity projectIdentity;
    
    @ManyToOne
    @JoinColumn(name = "admissibility_grid_id", nullable = false)
    private AdmissibilityGridEntity admissibilityGrid;
    
    @Column(name = "response_date", nullable = false)
    private Date responseDate;
    
    @Column(name = "responses", columnDefinition = "jsonb")
    private String responses; // JSON string containing criteria responses
    
    @Column(name = "verification_status", nullable = false)
    private boolean verificationStatus;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @OneToOne
    @JoinColumn(name = "derogation_id")
    private DerogationEntity derogation;
}