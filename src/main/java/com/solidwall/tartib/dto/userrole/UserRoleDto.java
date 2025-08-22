package com.solidwall.tartib.dto.userrole;

import com.solidwall.tartib.dto.role.RoleDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserRoleDto {
    private Long id;
    private Long userId;
    private String username;
    private RoleDto role;
        private String ministryAccessType;

}