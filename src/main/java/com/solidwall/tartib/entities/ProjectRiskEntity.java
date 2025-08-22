package com.solidwall.tartib.entities;

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
@Table(name = "project_risk")
@Entity
public class ProjectRiskEntity extends AbstractBaseEntity {

   @ManyToOne
    @JoinColumn(name = "project_identity_id", nullable = true)
  private ProjectIdentityEntity projectIdentity;

  @ManyToOne
  @JoinColumn(name = "category_risk_id", nullable = true)
  private CategoryRiskEntity categoryRisk;

  @Column(name = "name", length = 1500, nullable = true)
  private String name;

  @Column(name = "description", length = 1500, nullable = true, columnDefinition = "TEXT")
  private String description;

  @Column(name = "probability", length = 1500, nullable = true)
  private String probability;

  @Column(name = "gravity", length = 1500, nullable = true)
  private String gravity;



  @Column(name = "mitigation", length = 1500, nullable = true)
  private String mitigation;


}
