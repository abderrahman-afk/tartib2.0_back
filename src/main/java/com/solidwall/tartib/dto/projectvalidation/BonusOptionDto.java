package com.solidwall.tartib.dto.projectvalidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonusOptionDto {
    private String name;
    private Double percentage;
    private String description;
    private Boolean isApplied; // Whether this bonus was actually applied to the project
    private String category; // sectorial, geographic, grid
}