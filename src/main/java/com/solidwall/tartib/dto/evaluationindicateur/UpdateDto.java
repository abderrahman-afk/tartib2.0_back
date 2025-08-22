package com.solidwall.tartib.dto.evaluationindicateur;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

    private String name;
    private String code;
    private String description;
    private boolean isActive;
    private List<com.solidwall.tartib.dto.evaluationnorme.UpdateDto> normes;
}