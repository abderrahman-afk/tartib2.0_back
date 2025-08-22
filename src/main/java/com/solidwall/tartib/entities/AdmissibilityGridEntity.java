package com.solidwall.tartib.entities;

import java.util.List;

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
@Table(name = "admissibility_grid")
@Entity
public class AdmissibilityGridEntity extends AbstractBaseEntity {
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "admissibiltyGrid", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<AdmissibilityCriteriaEntity> critirias;

  @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
  private boolean isActive;

  @Column(name = "version", nullable = false)
  private Integer version = 1;

  @Column(name = "isTemplate", nullable = false)
  private boolean isTemplate = true;


}
