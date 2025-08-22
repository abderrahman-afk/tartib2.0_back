package com.solidwall.tartib.dto.roleAccess;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreateDto {

  private List<Long> access;

  private Long role;
  
}
