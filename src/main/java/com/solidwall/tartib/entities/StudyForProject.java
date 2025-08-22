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
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study_for_project")
@Entity
public class StudyForProject extends AbstractBaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectStudy_id", nullable = true, referencedColumnName = "id")
    private ProjectStudyEntity projectStudy;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "state", length = 255, nullable = true)
    private String state;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "realisation_date", nullable = true)
    private Date realisationDate;

    

}
