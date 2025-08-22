package com.solidwall.tartib.dto.nomenclatureGeographic.GeographicCategory;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateGeographicCategoryDto {
    @NotBlank
    private String code;

    @NotBlank
    private String title;

    private String description;

    @NotEmpty(message = "At least one gouvernorat is required")
    private Set<Long> gouvernoratIds;
}
