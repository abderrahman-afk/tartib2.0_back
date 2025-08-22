package com.solidwall.tartib.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

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
@Table(name = "role_assignable_roles")
@Entity
public class RoleAssignableRoleEntity extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference

    private RoleEntity role;

    @ManyToOne
     @JoinColumn(name = "assignable_role_id", nullable = false)
    private RoleEntity assignableRole;
}