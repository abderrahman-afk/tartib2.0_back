package com.solidwall.tartib.dto.userrole;

import com.solidwall.tartib.enums.MinistryAccessType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

  private Long user;

  private Long role;
  
  private MinistryAccessType ministryAccessType;

}
