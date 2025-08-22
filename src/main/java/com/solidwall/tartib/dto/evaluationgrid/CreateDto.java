package com.solidwall.tartib.dto.evaluationgrid;

import java.util.List;

import com.solidwall.tartib.enums.EvaluationGridState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

    private String name;
    private String description;
    private boolean isActive;
    private EvaluationGridState state = EvaluationGridState.brouillon; // Default value

    private String code;
    private int maxComponents;
    private int maxCrtieras;
    private int maxIndicator;
    private int maxNormes;
    private int maxNote;
    private List<com.solidwall.tartib.dto.evaluationcomponent.CreateDto> components;
};