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
@Table(name = "stakeholder")
@Entity
public class StakeHolderEntity extends AbstractBaseEntity {


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectZone_id", nullable = false, referencedColumnName = "id")
    private ProjectZoneEntity projectZone;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", length = 255, nullable = true, columnDefinition = "TEXT")
    private String type;

}
