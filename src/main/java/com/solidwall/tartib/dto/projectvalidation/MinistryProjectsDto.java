package com.solidwall.tartib.dto.projectvalidation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinistryProjectsDto {
    private String ministryName;
    private List<ProjectValidationDetailDto> projects;
}