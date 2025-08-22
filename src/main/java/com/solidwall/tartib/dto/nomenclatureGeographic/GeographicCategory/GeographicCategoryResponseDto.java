package com.solidwall.tartib.dto.nomenclatureGeographic.GeographicCategory;
import java.util.Set;

import com.solidwall.tartib.entities.GovernorateEntity;
 
import lombok.Data;
@Data
public class GeographicCategoryResponseDto {
    private Long id;
    private String code;
    private String title;
    private String description;
    private Set<GovernorateEntity> governorates;
}