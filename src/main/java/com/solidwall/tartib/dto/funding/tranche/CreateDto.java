package com.solidwall.tartib.dto.funding.tranche;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

    private String title;

    private String description;

    private float amount;

    private int disbursement;

    private Date year;

    private Long projectPlan;
}
