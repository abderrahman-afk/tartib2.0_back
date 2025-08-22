package com.solidwall.tartib.dto.project.logic;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

    private Long projectIdentity;
    private List<com.solidwall.tartib.dto.componentlogic.CreateDto> componentLogics;
    private String generalObjective;
    private String specific_objective;
    private String results;
    private String documentCadre;
    private String yearStart;
    private String yearEnd;
    private String documentPlanTravail;

}
