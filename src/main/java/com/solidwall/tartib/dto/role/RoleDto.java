package com.solidwall.tartib.dto.role;

import java.util.List;

import com.solidwall.tartib.entities.AccessEntity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Data
public class RoleDto {
 private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private List<AccessEntity> access;
}
