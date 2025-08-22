package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "cnap_validation_reserve")
@Getter
@Setter
@NoArgsConstructor
public class CnapValidationReserveEntity extends AbstractBaseEntity{
    @ManyToOne
    @JoinColumn(name = "project_identity_id", nullable = false)
    private ProjectIdentityEntity projectIdentity;
    
    @Column(name = "reserve", columnDefinition = "TEXT", nullable = false)
    private String reserve;
}
