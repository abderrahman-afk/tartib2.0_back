package com.solidwall.tartib.dto.component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

    private Long project;

    private Long projectPlan;

    private boolean isSubComponent;

    private Long component;

    private String name;

    private String code;

    private String description;


}
