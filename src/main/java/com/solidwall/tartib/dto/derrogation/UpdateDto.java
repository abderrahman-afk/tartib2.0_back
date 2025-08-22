package com.solidwall.tartib.dto.derrogation;

import com.solidwall.tartib.enums.DerogationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {
    private Long projectIdentityId;
    private Long admissibilityGridId;
    private String justification;
    private String requestedBy;
    private DerogationStatus status;
}
