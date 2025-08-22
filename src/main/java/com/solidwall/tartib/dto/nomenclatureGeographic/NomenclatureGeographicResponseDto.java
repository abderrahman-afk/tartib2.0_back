package com.solidwall.tartib.dto.nomenclatureGeographic;

import java.util.Date;
import java.util.List;

import com.solidwall.tartib.dto.nomenclatureGeographic.GeographicCategory.GeographicCategoryResponseDto;

import lombok.Data;

@Data
public class NomenclatureGeographicResponseDto {
    private Long id;
    private String code;
    private String title;
    private String justificationPath;
    private Integer year;
    private Boolean active;
    private List<GeographicCategoryResponseDto> categories;
    private Date createdAt;
    private Date updatedAt;
}