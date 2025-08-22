package com.solidwall.tartib.dto.ponderation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {
      Integer maxindicateur;
      Integer maxnote;
      Integer totalPoints;
      Long evaluationGridId;  // Added this field
      Boolean isActive;
      List<com.solidwall.tartib.dto.ponderationComponentDto.CreateDto> components;
}
