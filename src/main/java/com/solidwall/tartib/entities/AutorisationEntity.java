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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "autorisation")
@Entity
public class AutorisationEntity extends AbstractBaseEntity {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectStudy_id", nullable = true, referencedColumnName = "id")
    private ProjectStudyEntity projectStudy;

    @Column(name = "name", length = 255, nullable = true)
    private String name;
    @Column(name = "validateur", length = 255, nullable = true)
    private String validateur;
    @Column(name = "observation", length = 255, nullable = true)
    private String observation;

    @Column(name = "justification_path", length = 1000)
    private String justificationPath;
     
}
