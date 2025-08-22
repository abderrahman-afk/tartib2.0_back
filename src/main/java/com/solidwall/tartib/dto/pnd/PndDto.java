package com.solidwall.tartib.dto.pnd;

import java.util.Date;

import com.solidwall.tartib.entities.PndEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PndDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;
    // Note: We don't include pndAxis list here to break the circular reference
    
    public static PndDto fromEntity(PndEntity entity) {
        PndDto dto = new PndDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}