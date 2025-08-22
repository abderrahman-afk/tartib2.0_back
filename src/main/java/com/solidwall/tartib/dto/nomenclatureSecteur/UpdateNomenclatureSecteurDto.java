package com.solidwall.tartib.dto.nomenclatureSecteur;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solidwall.tartib.dto.nomenclatureSecteur.Secteur.UpdateSecteurDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Data
@Slf4j
 public class UpdateNomenclatureSecteurDto {
       @NotBlank
    private String code;
    
    @NotBlank
    private String title;
    
    @NotNull
    private Integer year;
    
    private MultipartFile justificationFile;
    
    private String secteurs; // JSON string
    
    @JsonIgnore
    public List<UpdateSecteurDto> getSecteursList() {
        try {
            if (secteurs == null || secteurs.isEmpty()) {
                return new ArrayList<>();
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(secteurs, 
                mapper.getTypeFactory().constructCollectionType(List.class, UpdateSecteurDto.class));
        } catch (Exception e) {
            log.error("Error parsing sectors JSON: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid sectors data: " + e.getMessage());
        }
    }
}
