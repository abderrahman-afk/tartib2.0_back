package com.solidwall.tartib.dto.roleAccess;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindDto {

  private Long accessId;
  private Long roleId;
  
  private String accessName;
  private String roleName;
  
}
