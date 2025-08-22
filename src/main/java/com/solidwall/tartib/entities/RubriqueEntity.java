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
@Table(name = "rubrique")
@Entity
public class RubriqueEntity  extends AbstractBaseEntity{

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "projectPlan_id", nullable = false, referencedColumnName = "id")
  private ProjectPlanEntity projectPlan;
  @Column(name = "name", nullable = true)
  private String name;
  
  @Column(name = "amount", nullable = true)
  private Long amount;
    
}
