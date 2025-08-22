package com.solidwall.tartib.dto.componentlogic;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDto {
    private Long projectLogic_id;

    private String name;

    private String description;

    private Long cout;


}
