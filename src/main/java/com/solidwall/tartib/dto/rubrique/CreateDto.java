package com.solidwall.tartib.dto.rubrique;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDto {
    private Long projectPlan_id;

    private String name;

    private Long amount;

}
