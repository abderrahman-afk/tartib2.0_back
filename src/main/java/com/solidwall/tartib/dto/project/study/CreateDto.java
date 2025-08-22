package com.solidwall.tartib.dto.project.study;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

  private Long projectIdentity;
  private List<com.solidwall.tartib.dto.autorisation.CreateDto> autorisations;
  private List<com.solidwall.tartib.dto.study.CreateDto> studies;


}
