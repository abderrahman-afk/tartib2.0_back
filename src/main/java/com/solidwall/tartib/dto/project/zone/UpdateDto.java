package com.solidwall.tartib.dto.project.zone;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {
 
  private String functionStatus;

  private String stakeholderRole;

  private String observation;

  private List<com.solidwall.tartib.dto.stakeholder.UpdateDto> stakeholders;

  private List<com.solidwall.tartib.dto.linkedProject.UpdateDto> linkedProjects;

  private List<Long> districts ;
  private List<Long> governorates ;
  private List<Long> delegations ;
  private List<Long> deanships ;

  private String justification;

}
