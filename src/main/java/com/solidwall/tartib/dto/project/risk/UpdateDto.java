package com.solidwall.tartib.dto.project.risk;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

  Long projectIdentity;

  Long categoryRisk;

  String name;

  String description;

  String probability;
  String gravity;

  String mitigation;
  
}
