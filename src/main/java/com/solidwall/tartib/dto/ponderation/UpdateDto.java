package com.solidwall.tartib.dto.ponderation;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpdateDto {
    Integer maxindicateur;
    Integer totalPoints;
    Long evaluationGridId;  // Added this field
    Integer maxnote;
    Boolean isActive;
    List<com.solidwall.tartib.dto.ponderationComponentDto.UpdateDto> components;
}
