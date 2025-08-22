package com.solidwall.tartib.dto.pndAxe;

import java.util.Date;

import com.solidwall.tartib.dto.pnd.PndDto;
import com.solidwall.tartib.entities.PndAxisEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PndAxisDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;
    private PndDto pnd;  // Include the PND, but as a DTO
    
    public static PndAxisDto fromEntity(PndAxisEntity entity) {
        PndAxisDto dto = new PndAxisDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        if (entity.getPnd() != null) {
            dto.setPnd(PndDto.fromEntity(entity.getPnd()));
        }
        
        return dto;
    }
}