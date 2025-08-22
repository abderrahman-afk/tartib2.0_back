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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "component_logic")
public class ComponentLogicEntity extends AbstractBaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectLogic",  referencedColumnName = "id")
    private ProjectLogicEntity projectLogic;

  @Column(name = "name", length = 255, nullable = true)
  private String name;

  @Column(name = "description", length = 255, nullable = true)
  private String description;


  @Column(name = "cout", length = 255, nullable = true)
  private Long cout;

  

}
