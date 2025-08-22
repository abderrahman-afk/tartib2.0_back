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
@Table(name = "pnd_axis")
@Entity
public class PndAxisEntity extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "pnd_id", nullable = false, referencedColumnName = "id")
    private PndEntity pnd;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "code", length = 255, nullable = true)
    private String code;

    @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
    private boolean isActive;
}
