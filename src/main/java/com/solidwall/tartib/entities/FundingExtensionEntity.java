package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import java.sql.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "funding_extension")
@AllArgsConstructor
@NoArgsConstructor
public class FundingExtensionEntity extends AbstractBaseEntity {

  @ManyToOne
  @JoinColumn(name = "project_funding_id", unique = true, nullable = true)
  private ProjectPlanEntity projectPlan;

  @Column(name = "sequence", length = 255, nullable = true)
  private int sequence;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
  private String description;

  @Column(name = "start_date", length = 255, nullable = true)
  private Date startDate;

  @Column(name = "end_date", length = 255, nullable = true)
  private Date endDate;

  @Column(name = "actual_date", length = 255, nullable = true)
  private Date actualDate;

}
