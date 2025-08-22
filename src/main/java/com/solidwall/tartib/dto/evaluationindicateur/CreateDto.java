package com.solidwall.tartib.dto.evaluationindicateur;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {
    private String name;
    private String code;
    private String description;
    private boolean isActive;
    private Long evaluationCritiriaId;
}