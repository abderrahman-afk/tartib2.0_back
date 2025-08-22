package com.solidwall.tartib.entities;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Entity
public class RoleEntity extends AbstractBaseEntity {

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "description", nullable = true, columnDefinition = "TEXT")
  private String description;

  @Column(name = "isActive", nullable = false, columnDefinition = "boolean")
  private boolean isActive;

  @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonBackReference
  private List<UserRoleEntity> userRoles;
  @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<RoleAccessEntity> roleAccess;
  
  @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<RolePermissionEntity> rolePermission;
  @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
private List<RoleAssignableRoleEntity> assignableRoles;
}
