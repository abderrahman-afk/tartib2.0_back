package com.solidwall.tartib.dto.access;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

  private String name;

  private String value;

  private String description;

  private boolean active;
  
}
