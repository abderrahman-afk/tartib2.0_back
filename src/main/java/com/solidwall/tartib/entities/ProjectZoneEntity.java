package com.solidwall.tartib.entities;

import java.util.ArrayList;
import java.util.List;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "project_zone")
@Entity
public class ProjectZoneEntity extends AbstractBaseEntity {


   @OneToOne
    @JoinColumn(name = "project_identity_id", nullable = true, unique = true)
    private ProjectIdentityEntity projectIdentity;
    @Column(name = "fonction_statut", length = 255, nullable = true)
    private String functionStatus;

    @Column(name = "observation", length = 255, nullable = true)
    private String observation;

    @Column(name = "justification", length = 255, nullable = true)
    private String justification;

    @OneToMany(mappedBy = "projectZone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StakeHolderEntity> stakeholders = new ArrayList<>();

    @OneToMany(mappedBy = "projectZone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LinkedProjectEntity> linkedProjects = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "project_zone_district", joinColumns = @JoinColumn(name = "project_zone_id"), inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<DistrictEntity> districts = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "project_zone_governorate", joinColumns = @JoinColumn(name = "project_zone_id"), inverseJoinColumns = @JoinColumn(name = "governorate_id"))
    private List<GovernorateEntity> governorates = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "project_zone_delegation", joinColumns = @JoinColumn(name = "project_zone_id"), inverseJoinColumns = @JoinColumn(name = "delegation_id"))
    private List<DelegationEntity> delegations = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "project_zone_deanship", joinColumns = @JoinColumn(name = "project_zone_id"), inverseJoinColumns = @JoinColumn(name = "deanship_id"))
    private List<DeanshipEntity> deanships = new ArrayList<>();


 
}
