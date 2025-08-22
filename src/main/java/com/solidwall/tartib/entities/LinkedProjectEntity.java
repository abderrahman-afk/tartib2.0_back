package com.solidwall.tartib.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "linked_project")
public class LinkedProjectEntity extends AbstractBaseEntity {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectZone_id", nullable = false, referencedColumnName = "id")
    private ProjectZoneEntity projectZone;

    @Column(name = "project_code", nullable = true)
    private String projectCode;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

}
