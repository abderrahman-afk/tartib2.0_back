package com.solidwall.tartib.dto.admissibilitygrid;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

    private String name;

    private String description;
    
    private List<com.solidwall.tartib.dto.admissibilitycritiria.CreateDto> critirias;

    private boolean active;



}
