package com.solidwall.tartib.dto.roleAssignableRole;

import java.util.List;

import lombok.Data;

@Data
public class AssignableRolesResponse {
    private Long roleId;
    private List<Long> assignableRoleIds;
}