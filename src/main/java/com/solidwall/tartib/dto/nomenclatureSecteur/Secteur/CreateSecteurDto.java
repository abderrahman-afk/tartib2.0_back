package com.solidwall.tartib.dto.nomenclatureSecteur.Secteur;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateSecteurDto {
    @NotBlank
    private String code;
    
    @NotBlank
    private String title;
    
    private String description;
    
    @NotEmpty(message = "At least one minister is required")
    private Set<Long> ministerIds;
}
