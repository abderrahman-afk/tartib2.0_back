package com.solidwall.tartib.dto.delegation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

  private String name;

  private String code;

  private String description;

  private boolean active;


  private Long governorate;
  
}
