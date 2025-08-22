package com.solidwall.tartib.dto.evaluationcomponent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {
    private String name;
    private String description;
    private boolean isActive;
    private Long evaluationGridId;
}