package com.solidwall.tartib.dto.project.zone;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {
  private Long projectIdentity;
  private String functionStatus;


  private String observation;

  private List<com.solidwall.tartib.dto.stakeholder.CreateDto> stakeholders;

  private List<com.solidwall.tartib.dto.linkedProject.CreateDto> linkedProjects;

  private List<Long> districts;
  private List<Long> governorates;
  private List<Long> delegations;
  private List<Long> deanships;

  private String justification;

}
