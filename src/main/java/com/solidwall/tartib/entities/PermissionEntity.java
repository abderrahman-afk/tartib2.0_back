package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "permission")
public class PermissionEntity extends AbstractBaseEntity {

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "value", length = 250, nullable = false)
    private String value;

    @Column(name = "isActive", length = 255, nullable = false, columnDefinition = "boolean")
    private boolean isActive;
  
}
