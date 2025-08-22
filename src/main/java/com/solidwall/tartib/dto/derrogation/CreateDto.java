package com.solidwall.tartib.dto.derrogation;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CreateDto {
    private Long projectIdentityId;
    private Long admissibilityGridId;
    private String justification;
    private String requestedBy;
}
