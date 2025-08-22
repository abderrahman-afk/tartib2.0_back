package com.solidwall.tartib.dto.exportation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationResultsExportDto {
    
    private String systemReference;
    private String generationDate;
    private List<ProjectResultExportDto> projects;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectResultExportDto {
        private Integer rank;
        private String projectCode;
        private String projectTitle;
        private String ministry;
        private String governorate;
        private String totalCost;
        private Double globalScore;
        private Double bonifiedScore;
        private String cnapDecision;
        private String reserves;
    }
}