package com.solidwall.tartib.dto.sector;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

    private String name;

    private String code;

    private String description;

    private boolean active;

    private Long ProjectIdentity;
}
