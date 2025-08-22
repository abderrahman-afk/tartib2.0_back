package com.solidwall.tartib.dto.nomenclatureGeographic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solidwall.tartib.dto.nomenclatureGeographic.GeographicCategory.CreateGeographicCategoryDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CreateNomenclatureGeographicDto {
    @NotBlank
    private String code;

    @NotBlank
    private String title;

    @NotNull
    private Integer year;

    private MultipartFile justificationFile;

    private String geographicCategories; // JSON string

    @JsonIgnore
    public List<CreateGeographicCategoryDto> getGeographicCategoriesList() {
        try {
            if (geographicCategories == null || geographicCategories.isEmpty()) {
                return new ArrayList<>();
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(geographicCategories,
                    mapper.getTypeFactory().constructCollectionType(List.class, CreateGeographicCategoryDto.class));
        } catch (Exception e) {
            log.error("Error parsing governorates JSON: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid governorates data: " + e.getMessage());
        }
    }
}