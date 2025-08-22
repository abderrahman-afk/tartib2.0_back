package com.solidwall.tartib.services;

import com.solidwall.tartib.dto.exportation.AdminEvaluationScoresToExportDto;
import com.solidwall.tartib.dto.exportation.ClassificationResultsExportDto;
import com.solidwall.tartib.dto.exportation.ClassificationResultsExportDto.ProjectResultExportDto;
import com.solidwall.tartib.dto.exportation.ProjectToExportDto;
import com.solidwall.tartib.entities.*;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.implementations.ExportationImplementation;
import com.solidwall.tartib.repositories.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportationService implements ExportationImplementation {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String formatDate(Date date) {
        if (date == null)
            return "";
        return date.toLocalDate().format(DATE_FORMAT);
    }

    private String formatDateTime(java.util.Date date) {
        if (date == null)
            return "";
        return date.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DATETIME_FORMAT);
    }

    @Autowired
    private ProjectIdentityRepository projectIdentityRepository;

    @Autowired
    private ProjectLogicRepository projectLogicRepository;

    @Autowired
    private ProjectZoneRepository projectZoneRepository;

    @Autowired
    private ProjectPlanRepository projectPlanRepository;

    @Autowired
    private ProjectRiskRepository projectRiskRepository;

    @Autowired
    private ProjectStudyRepository projectStudyRepository;

    @Autowired
    private AdminEvaluationResponseRepository adminEvaluationResponseRepository;
    @Autowired
    private GeneratedClassificationRepository generatedClassificationRepository;
    @Autowired
    private CnapValidationReserveRepository cnapValidationReserveRepository;

    // export project FP
    public ProjectToExportDto exportProjectData(long projectId) {

        Optional<ProjectIdentityEntity> projectIdentityOpt = projectIdentityRepository.findById(projectId);

        if (projectIdentityOpt.isEmpty()) {
            return new ProjectToExportDto();
        }

        ProjectIdentityEntity projectIdentity = projectIdentityOpt.get();

        // Build the main DTO
        ProjectToExportDto.ProjectToExportDtoBuilder builder = ProjectToExportDto.builder()
                .projectCode(projectIdentity.getCode())
                .projectTitle(projectIdentity.getName())
                .ministry(projectIdentity.getMinister().getName())
                .porteuseStructure(projectIdentity.getResponsibleName())
                .responsible(projectIdentity.getResponsibleEmail())
                .description(projectIdentity.getDescription())
                .generationDate(LocalDateTime.now().format(DATETIME_FORMAT))
                .contact(projectIdentity.getResponsiblePhone() != null ? projectIdentity.getResponsiblePhone() : "");

        // Add location information
        Optional<ProjectZoneEntity> projectZone = projectZoneRepository.findByProjectIdentity(projectIdentity);
        if (projectZone.isPresent()) {
            ProjectZoneEntity zone = projectZone.get();

            List<String> districtNames = zone.getDistricts().stream()
                    .map(DistrictEntity::getName)
                    .collect(Collectors.toList());

            List<String> governorateNames = zone.getGovernorates().stream()
                    .map(GovernorateEntity::getName)
                    .collect(Collectors.toList());

            List<String> delegationNames = zone.getDelegations().stream()
                    .map(DelegationEntity::getName)
                    .collect(Collectors.toList());

            ProjectToExportDto.LocationToExportDto location = ProjectToExportDto.LocationToExportDto.builder()
                    .district(districtNames.isEmpty() ? "" : String.join(", ", districtNames))
                    .gouvernorats(governorateNames)
                    .delegations(delegationNames)
                    .zoneInfluenceDescription(zone.getObservation())
                    .build();

            builder.location(location);
        }

        // Add intervention logic
        Optional<ProjectLogicEntity> projectLogic = projectLogicRepository.findByProjectIdentity(projectIdentity);
        if (projectLogic.isPresent()) {
            ProjectLogicEntity logic = projectLogic.get();

            List<String> specificObjectives = new ArrayList<>();
            if (logic.getSpecific_objective() != null) {
                specificObjectives.add(logic.getSpecific_objective());
            }

            List<String> expectedResults = new ArrayList<>();
            if (logic.getResults() != null) {
                expectedResults.add(logic.getResults());
            }

            ProjectToExportDto.InterventionLogicToExportDto interventionLogic = ProjectToExportDto.InterventionLogicToExportDto
                    .builder()
                    .generalObjective(logic.getGeneralObjective())
                    .specificObjectives(specificObjectives)
                    .expectedResults(expectedResults)
                    .build();

            builder.interventionLogic(interventionLogic);
        }

        // Add work plan
        if (projectLogic.isPresent()) {
            ProjectLogicEntity logic = projectLogic.get();
            ProjectToExportDto.WorkPlanToExportDto workPlan = ProjectToExportDto.WorkPlanToExportDto.builder()
                    .startYear(logic.getYearStart())
                    .endYear(logic.getYearEnd())
                    .build();
            builder.workPlan(workPlan);
        }

        // Add financing plan
        Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findByProjectIdentity(projectIdentity);
        if (projectPlan.isPresent()) {
            ProjectPlanEntity plan = projectPlan.get();

            List<ProjectToExportDto.FinancingPlanToExportDto.FinancingSourceToExportDto> financingSources = plan
                    .getFinancialSource().stream()
                    .map(source -> ProjectToExportDto.FinancingPlanToExportDto.FinancingSourceToExportDto.builder()
                            .source(source.getBailleur() != null ? source.getBailleur() : source.getType())
                            .totalAmount(formatMoney(source.getMontantDinars()))
                            .build())
                    .collect(Collectors.toList());

            ProjectToExportDto.FinancingPlanToExportDto financingPlan = ProjectToExportDto.FinancingPlanToExportDto
                    .builder()
                    .totalCost(formatMoney(plan.getCoutTotale()))
                    .financingSources(financingSources)
                    .build();

            builder.financingPlan(financingPlan);
        }

        // Add components from project logic
        List<ProjectToExportDto.ComponentToExportDto> components = new ArrayList<>();
        if (projectLogic.isPresent()) {
            ProjectLogicEntity logic = projectLogic.get();
            components = logic.getComponentLogics().stream()
                    .map(comp -> ProjectToExportDto.ComponentToExportDto.builder()
                            .componentName(comp.getName())
                            .cost(formatMoney(comp.getCout()))
                            .build())
                    .collect(Collectors.toList());
        }
        builder.components(components);

        // Add risk analysis
        List<ProjectRiskEntity> risks = projectRiskRepository.findAllByProjectIdentity(projectIdentity);
        List<ProjectToExportDto.RiskAnalysisToExportDto> riskAnalysis = risks.stream()
                .map(risk -> ProjectToExportDto.RiskAnalysisToExportDto.builder()
                        .category(risk.getCategoryRisk() != null ? risk.getCategoryRisk().getName() : "")
                        .risk(risk.getName())
                        .probability(risk.getProbability())
                        .severity(risk.getGravity())
                        .mitigationMeasures(risk.getMitigation())
                        .build())
                .collect(Collectors.toList());
        builder.riskAnalysis(riskAnalysis);

        // Add studies
        Optional<ProjectStudyEntity> projectStudy = projectStudyRepository.findByProjectIdentity(projectIdentity);
        List<ProjectToExportDto.StudyToExportDto> studies = new ArrayList<>();
        if (projectStudy.isPresent()) {
            studies = projectStudy.get().getStudies().stream()
                    .map(study -> ProjectToExportDto.StudyToExportDto.builder()
                            .title(study.getName())
                            .realizationDate(formatDate(study.getRealisationDate()))
                            .updateDate(formatDateTime(study.getUpdatedAt()))
                            .observations(study.getDescription())
                            .build())
                    .collect(Collectors.toList());
        }
        builder.studies(studies);

        return builder.build();
    }

    // export evealuation
    public AdminEvaluationScoresToExportDto exportAdminEvaluationScores(long projectId) {

        // Find admin evaluation response for the project
        Optional<AdminEvaluationResponseEntity> adminEvaluationOpt = adminEvaluationResponseRepository
                .findByProjectId(projectId);

        if (adminEvaluationOpt.isEmpty()) {
            return AdminEvaluationScoresToExportDto.builder()
                    .projectId(projectId)
                    .build();
        }

        AdminEvaluationResponseEntity adminEvaluation = adminEvaluationOpt.get();
        ProjectIdentityEntity project = adminEvaluation.getProject();
        EvaluationGridEntity evaluationGrid = adminEvaluation.getEvaluationGrid();

        // Build the main DTO with project and grid information
        AdminEvaluationScoresToExportDto.AdminEvaluationScoresToExportDtoBuilder builder = AdminEvaluationScoresToExportDto
                .builder()
                .projectId(projectId)
                .projectCode(project != null ? project.getCode() : "")
                .projectName(project != null ? project.getName() : "")
                .evaluationGridId(evaluationGrid != null ? evaluationGrid.getId() : null)
                .evaluationGridName(evaluationGrid != null ? evaluationGrid.getName() : "")
                .evaluationGridReference(evaluationGrid != null ? evaluationGrid.getCode() : "")
                .adminGlobalScore(adminEvaluation.getAdminGlobalScore())
                .originalGlobalScore(adminEvaluation.getOriginalGlobalScore());

        // Build component scores with criteria breakdown
        List<AdminEvaluationScoresToExportDto.ComponentScoreDto> componentScores = new ArrayList<>();

        if (adminEvaluation.getComponents() != null) {
            for (ResponseAdminEvaluationComponentEntity component : adminEvaluation.getComponents()) {

                // Build criteria scores for each component
                List<AdminEvaluationScoresToExportDto.CriteriaScoreDto> criteriaScores = new ArrayList<>();

                if (component.getCriteria() != null) {
                    for (ResponseAdminEvaluationCriteriaEntity criteria : component.getCriteria()) {
                        AdminEvaluationScoresToExportDto.CriteriaScoreDto criteriaScore = AdminEvaluationScoresToExportDto.CriteriaScoreDto
                                .builder()
                                .criteriaId(criteria.getCriteria() != null ? criteria.getCriteria().getId() : null)
                                .criteriaName(criteria.getCriteria() != null ? criteria.getCriteria().getName() : "")
                                .adminCriteriaScore(criteria.getAdminCriteriaScore())
                                .originalCriteriaScore(criteria.getOriginalCriteriaScore())
                                .build();
                        criteriaScores.add(criteriaScore);
                    }
                }

                // Create component DTO with criteria
                AdminEvaluationScoresToExportDto.ComponentScoreDto componentScore = AdminEvaluationScoresToExportDto.ComponentScoreDto
                        .builder()
                        .componentId(component.getComponent() != null ? component.getComponent().getId() : null)
                        .componentName(component.getComponent() != null ? component.getComponent().getName() : "")
                        .adminComponentScore(component.getAdminComponentScore())
                        .originalComponentScore(component.getOriginalComponentScore())
                        .criteria(criteriaScores)
                        .build();

                componentScores.add(componentScore);
            }
        }

        builder.components(componentScores);

        return builder.build();
    }

    // export classification results
    // Add this method to your ExportationService.java
    @Override
  public ClassificationResultsExportDto exportClassificationResults() {
    log.info("Starting export of latest classification results");

    try {
        // Get the latest generated classification
        GeneratedClassificationEntity latestClassification = generatedClassificationRepository
                .findTopByOrderByGenerationDateDesc()
                .orElseThrow(() -> new RuntimeException("No generated classification found"));

        log.info("Found latest classification: {} generated on {}",
                latestClassification.getName(), latestClassification.getGenerationDate());

        // Build the main DTO
        ClassificationResultsExportDto.ClassificationResultsExportDtoBuilder builder = ClassificationResultsExportDto
                .builder()
                .systemReference(latestClassification.getClassificationSystem() != null
                        ? latestClassification.getClassificationSystem().getCode()
                        : "N/A")
                .generationDate(formatDateTime(latestClassification.getGenerationDate()));

        // Build project results list - only include projects with CNAP validation status
        List<ClassificationResultsExportDto.ProjectResultExportDto> projectResults = latestClassification
                .getProjectClassements().stream()
                .filter(pc -> {
                    ProjectStaut status = pc.getProjectIdentity().getStatut();
                    return status == ProjectStaut.validé_par_cnap || 
                           status == ProjectStaut.validé_avec_reserve || 
                           status == ProjectStaut.rejeté_par_cnap;
                })
                .sorted((p1, p2) -> Integer.compare(p1.getRang(), p2.getRang())) // Sort by rank
                .map(this::mapProjectClassementToExport)
                .collect(Collectors.toList());

        builder.projects(projectResults);

        log.info("Successfully exported {} project results with CNAP validation status", projectResults.size());
        return builder.build();

    } catch (Exception e) {
        log.error("Error exporting classification results: {}", e.getMessage(), e);
        throw new RuntimeException("Failed to export classification results: " + e.getMessage());
    }
}
    @Override
    public ClassificationResultsExportDto.ProjectResultExportDto mapProjectClassementToExport(
            ProjectClassementEntity projectClassement) {

        ProjectIdentityEntity project = projectClassement.getProjectIdentity();

        return ClassificationResultsExportDto.ProjectResultExportDto.builder()
                .rank(projectClassement.getRang())
                .projectCode(project.getCode())
                .projectTitle(project.getName())
                .ministry(project.getMinister() != null ? project.getMinister().getName() : "N/A")
                .governorate(getProjectGovernorates(project))
                .totalCost(formatMoney(getProjectTotalCost(project)))
                .globalScore(projectClassement.getInitialScore())
                .bonifiedScore(Math.round(projectClassement.getScoreBonifie() * 10.0) / 10.0)
                .cnapDecision(project.getStatut().getDisplayName()) // To be filled based on validation status
                .reserves(cnapValidationReserveRepository.findByProjectIdentity(project).map(CnapValidationReserveEntity::getReserve).orElse("")) // To be filled based on reserves
                .build();
    }

    @Override
    public String getProjectGovernorates(ProjectIdentityEntity project) {
        try {
            ProjectZoneEntity projectZone = projectZoneRepository.findByProjectIdentity(project)
                    .orElseThrow(() -> new RuntimeException("Project zone not found for project " + project.getCode()));
            if (projectZone != null && projectZone.getGovernorates() != null) {
                return projectZone.getGovernorates().stream()
                        .map(GovernorateEntity::getName)
                        .collect(Collectors.joining(", "));
            }
        } catch (Exception e) {
            log.warn("Error getting governorates for project {}: {}", project.getCode(), e.getMessage());
        }
        return "N/A";
    }

    @Override
    public Long getProjectTotalCost(ProjectIdentityEntity project) {
        try {
            ProjectPlanEntity projectPlan = projectPlanRepository.existsByProjectIdentityId(project.getId())
                    ? projectPlanRepository.findByProjectIdentity(project).orElse(null)
                    : null;
            // Check if project has financial plan with total cost
            if (projectPlan != null && projectPlan.getCoutTotale() != null) {
                return projectPlan.getCoutTotale();
            }

            return 0L;
        } catch (Exception e) {
            log.warn("Error calculating total cost for project {}: {}", project.getCode(), e.getMessage());
            return 0L;
        }
    }

    // helper
    public String formatMoney(Long amount) {
        if (amount == null || amount == 0)
            return "0 DT";

        // Convert to string and add spaces every 3 digits from right
        String numberStr = amount.toString();
        StringBuilder formatted = new StringBuilder();

        int count = 0;
        for (int i = numberStr.length() - 1; i >= 0; i--) {
            if (count > 0 && count % 3 == 0) {
                formatted.insert(0, ' ');
            }
            formatted.insert(0, numberStr.charAt(i));
            count++;
        }

        return formatted.toString() + " DT";
    }

}