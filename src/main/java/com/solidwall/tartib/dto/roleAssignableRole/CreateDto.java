package com.solidwall.tartib.dto.roleAssignableRole;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreateDto {
    private Long role;
    private List<Long> assignableRoles;
}