package com.solidwall.tartib.dto.evaluationcomponent;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

    private String name;
    private String description;
    private boolean isActive;
    private List<com.solidwall.tartib.dto.evaluationcritiria.UpdateDto> critirias;
}