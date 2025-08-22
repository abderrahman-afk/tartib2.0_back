package com.solidwall.tartib.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study")
@Entity
public class StudyEntity extends AbstractBaseEntity {

    @JsonIgnore
    @ManyToMany(mappedBy = "studies")
    private List<ProjectScaleEntity> projects;
     
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "state", length = 255, nullable = true)
    private String state;

    @Column(name = "description", length = 255, nullable = true)
    private String description;
    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
    @Column(name = "realisation_date", nullable = true)
    private Date realisationDate;

}
