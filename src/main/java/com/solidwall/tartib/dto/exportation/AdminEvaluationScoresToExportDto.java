package com.solidwall.tartib.dto.exportation;


import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AdminEvaluationScoresToExportDto {

    // Project Information
    private Long projectId;
    private String projectCode;
    private String projectName;
    
    // Evaluation Grid Information
    private Long evaluationGridId;
    private String evaluationGridName;
    private String evaluationGridReference;
    
    // Global Scores
    private Double adminGlobalScore;
    private Double originalGlobalScore;
        // Generation Information
    private String generationDate;
    // Components with their scores and criteria
    private List<ComponentScoreDto> components;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentScoreDto {
        private Long componentId;
        private String componentName;
        private Double adminComponentScore;
        private Double originalComponentScore;
        private List<CriteriaScoreDto> criteria;
    }
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteriaScoreDto {
        private Long criteriaId;
        private String criteriaName;
        private Double adminCriteriaScore;
        private Double originalCriteriaScore;
     }
    
 
}
