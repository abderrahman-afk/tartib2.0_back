package com.solidwall.tartib.dto.rolePermission;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpdateDto {

  private List<Long> permission;

  private Long role;
  
}
