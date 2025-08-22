package com.solidwall.tartib.dto.ponderationComponentDto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {
      String name;
      Double numberOfPoints;
      Double percentage;
      List<com.solidwall.tartib.dto.ponderationCritiria.CreateDto> criterias;
}
