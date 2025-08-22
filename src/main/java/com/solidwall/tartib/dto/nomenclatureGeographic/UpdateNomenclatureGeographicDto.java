package com.solidwall.tartib.dto.nomenclatureGeographic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solidwall.tartib.dto.nomenclatureGeographic.GeographicCategory.UpdateGeographicCategoryDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class UpdateNomenclatureGeographicDto {
    @NotBlank
    private String code;

    @NotBlank
    private String title;

    @NotNull
    private Integer year;

    private MultipartFile justificationFile;

    private String geographicCategories; // JSON string

    @JsonIgnore
    public List<UpdateGeographicCategoryDto> getGeographicCategoryList() {
        try {
            if (geographicCategories == null || geographicCategories.isEmpty()) {
                return new ArrayList<>();
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(geographicCategories,
                    mapper.getTypeFactory().constructCollectionType(List.class, UpdateGeographicCategoryDto.class));
        } catch (Exception e) {
            log.error("Error parsing geographicCategories JSON: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid geographicCategories data: " + e.getMessage());
        }
    }
}
