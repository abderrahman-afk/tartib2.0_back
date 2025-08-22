package com.solidwall.tartib.dto.deanship;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

  private String name;

  private String code;

  private String description;

  private boolean active;

  private Long district;

  private Long governorate;

  private Long delegation;
  
}
