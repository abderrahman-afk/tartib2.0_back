package com.solidwall.tartib.dto.roleAccess;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

  private List<Long> access;

  private Long role;
  
}
