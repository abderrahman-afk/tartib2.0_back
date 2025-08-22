package com.solidwall.tartib.dto.admissibilitygrid;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

    private List<com.solidwall.tartib.dto.admissibilitycritiria.UpdateDto> critirias;

    private String name;

    private String description;

    private boolean active;

}
