package com.solidwall.tartib.dto.classificationGeneration;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateClassificationDto {
    
    @NotBlank(message = "Name is required")
    private String name;
    
 
    
    private String description;
    
    @NotNull(message = "Classification system ID is required")
    private Long classificationSystemId;
}