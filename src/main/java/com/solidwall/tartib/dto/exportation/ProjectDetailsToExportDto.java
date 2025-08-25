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
public class ProjectDetailsToExportDto {

    // Header Information
    private String projectCode;
    private String projectTitle;
    private String ministry;
    private String porteuseStructure;
    private String responsible;
    private String description;
    private String generationDate;
    private String contact;

    // Localization
    private LocationToExportDto location;

    // Intervention Logic
    private InterventionLogicToExportDto interventionLogic;

    // Components
    private List<ComponentToExportDto> components;

    // Work Plan
    private WorkPlanToExportDto workPlan;

    // Financing Plan
    private FinancingPlanToExportDto financingPlan;

    // Risk Analysis
    private List<RiskAnalysisToExportDto> riskAnalysis;

    // Studies
    private List<StudyToExportDto> studies;

    // Admin Evaluation
    private AdminEvaluationScoresToExportDto adminEvaluation;

    // Classification
    private Integer rank;
    private Double bonifiedScore;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationToExportDto {
        private String district;
        private List<String> gouvernorats;
        private List<String> delegations;
        private String zoneInfluenceDescription;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterventionLogicToExportDto {
        private String generalObjective;
        private List<String> specificObjectives;
        private List<String> expectedResults;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentToExportDto {
        private String componentName;
        private String cost;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkPlanToExportDto {
        private String startYear;
        private String endYear;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancingPlanToExportDto {
        private String totalCost;
        private List<FinancingSourceToExportDto> financingSources;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class FinancingSourceToExportDto {
            private String source;
            private String totalAmount;
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskAnalysisToExportDto {
        private String category;
        private String risk;
        private String probability;
        private String severity;
        private String mitigationMeasures;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyToExportDto {
        private String title;
        private String realizationDate;
        private String updateDate;
        private String observations;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminEvaluationScoresToExportDto {
        private String projectCode;
        private String projectName;
        private String evaluationGridName;
        private String evaluationGridReference;
        private Double adminGlobalScore;
        private Double originalGlobalScore;
        private String generationDate;
        private List<ComponentScoreDto> components;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentScoreDto {
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
        private String criteriaName;
        private Double adminCriteriaScore;
        private Double originalCriteriaScore;
    }
}
