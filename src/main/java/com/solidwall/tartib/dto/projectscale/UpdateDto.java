package com.solidwall.tartib.dto.projectscale;

import java.util.List;
 
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {
    private String name;
    private Long maximumBudget;
    private Long minimumBudget;
    private String description;
    private List<Long> studies;
    private boolean isActive;
  
}
