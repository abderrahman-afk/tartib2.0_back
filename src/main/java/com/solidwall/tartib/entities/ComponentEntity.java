package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "component")
@Entity
public class ComponentEntity extends AbstractBaseEntity {

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = true)
  private ProjectEntity project;

  @ManyToOne
  @JoinColumn(name = "project_plan_id", nullable = true)
  private ProjectPlanEntity projectPlan;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "component_id", nullable = true)
  private ComponentEntity component;

  @Column(name = "name", length = 255, nullable = true)
  private String name;

  @Column(name = "code", length = 255, nullable = true)
  private String code;

  @Column(name = "description", nullable = true, columnDefinition = "TEXT")
  private String description;

  @Column(name = "is_sub_component", nullable = true, columnDefinition = "boolean")
  private boolean isSubComponent;

}
