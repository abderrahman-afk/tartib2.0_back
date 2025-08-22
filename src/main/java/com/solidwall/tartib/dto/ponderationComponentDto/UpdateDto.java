package com.solidwall.tartib.dto.ponderationComponentDto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpdateDto {
    String name;
    Double numberOfPoints;
    Double percentage;
    List<com.solidwall.tartib.dto.ponderationCritiria.UpdateDto> criterias;
}
