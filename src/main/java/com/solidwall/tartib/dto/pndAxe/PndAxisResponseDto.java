package com.solidwall.tartib.dto.pndAxe;

import com.solidwall.tartib.entities.PndAxisEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PndAxisResponseDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private boolean active;
    private Long pndId;
    private String pndName;
    
    // You can add more PND fields if needed
    
    public static PndAxisResponseDto fromEntity(PndAxisEntity entity) {
        PndAxisResponseDto dto = new PndAxisResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        
        if (entity.getPnd() != null) {
            dto.setPndId(entity.getPnd().getId());
            dto.setPndName(entity.getPnd().getName());
        }
        
        return dto;
    }
}