package com.solidwall.tartib.dto.evaluationnorme;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateDto {
    private String name;
    private String description;
    private boolean isActive;
    private Long evaluationIndicateurId;
    private int note;

}