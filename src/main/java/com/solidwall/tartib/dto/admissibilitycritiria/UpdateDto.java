package com.solidwall.tartib.dto.admissibilitycritiria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

    private Long grid;

    private String name;

    private String description;
    
    private String requirementLevel;
    private boolean isActive;

}
