package com.solidwall.tartib.entities;

import java.sql.Date;

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
@Table(name = "funding_tranches")
@Entity
public class FundingTrancheEntity extends AbstractBaseEntity {

   
    @Column(name = "title", length = 255, nullable = true)
    private String title;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "amount", length = 255, nullable = true)
    private float amount;

    @Column(name = "disbursement", length = 255, nullable = true)
    private int disbursement;

    @Column(name = "year", length = 255, nullable = true)
    private Date year;


    @ManyToOne
    @JoinColumn(name = "project_plan_id", unique = true, nullable = true)
    private ProjectPlanEntity projectPlan;
}
