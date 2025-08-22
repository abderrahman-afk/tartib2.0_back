package com.solidwall.tartib.dto.projectvalidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CNAPValidationRequestDto {
    private Long projectId;
    private String status; // valide_par_cnap, valide_avec_reserve, rejete_par_cnap
    private String comment; // Optional validation comment
}