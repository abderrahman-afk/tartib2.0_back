package com.solidwall.tartib.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import com.solidwall.tartib.enums.ProjectStaut;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_identity")
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ProjectIdentityEntity extends AbstractBaseEntity {

    @Column(name = "code", length = 255, nullable = true)
    private String code;

    @Column(name = "name", length = 255, nullable = true)
    private String name;

    @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "typology_id", nullable = true)
    private TypologyEntity typology;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = true)
    private SectorEntity sector;

    @ManyToOne
    @JoinColumn(name = "minister_id", nullable = true)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

    private MinisterEntity minister;

    @Column(name = "minister_name", length = 255, nullable = true)
    private String ministerName;

    private String organisation;

    @Column(name = "responsible_name", length = 255, nullable = true)
    private String responsibleName;

    @Column(name = "responsible_email", length = 255, nullable = true)
    private String responsibleEmail;

    @Column(name = "responsible_phone", length = 255, nullable = true)
    private String responsiblePhone;

    @Column(name = "management_unitname", length = 255, nullable = true)
    private String managementUnitName;

    @Column(name = "project_manager_name", length = 255, nullable = true)
    private String projectManagerName;

    @Column(name = "project_manager_email", length = 255, nullable = true)
    private String projectManagerEmail;

    @Column(name = "project_manager_phone", length = 255, nullable = true)
    private String projectManagerPhone;

    @Column(name = "po_name", length = 255, nullable = true)
    private String projectOwnerName;

    @Column(name = "po_email", length = 255, nullable = true)
    private String projectOwnerEmail;

    @Column(name = "po_phone", length = 255, nullable = true)
    private String projectOwnerPhone;

    @Column(name = "archived", nullable = false)
    private boolean archived = false; // Default to false
    
    @ManyToOne
    @JoinColumn(name = "category_envergure_id", nullable = true)
    private CategoryEnvergureEntity envergure;

    @Column(name = "simep", length = 255, nullable = true)
    private Boolean simep;

    @Column(name = "statut", length = 255, nullable = true)
    @Enumerated(EnumType.STRING)
    private ProjectStaut statut;

    public String getStatusDisplay() {
        return statut != null ? statut.getDisplayName() : null;
    }
}
