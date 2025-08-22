package com.solidwall.tartib.dto.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

  private String name;

  private String value;

  private String description;

  private boolean active;
  
}
