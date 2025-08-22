package com.solidwall.tartib.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.projectvalidation.BonusOptionDto;
import com.solidwall.tartib.dto.projectvalidation.GridBonusGroupDto;
import com.solidwall.tartib.dto.projectvalidation.MinistryProjectsDto;
import com.solidwall.tartib.dto.projectvalidation.ProjectBonusAnalysisDto;
import com.solidwall.tartib.dto.projectvalidation.ProjectScoreDataDto;
import com.solidwall.tartib.dto.projectvalidation.ProjectValidationDataDto;
import com.solidwall.tartib.dto.projectvalidation.ProjectValidationDetailDto;
import com.solidwall.tartib.entities.AdminEvaluationResponseEntity;
import com.solidwall.tartib.entities.ClassificationEntity;
import com.solidwall.tartib.entities.CnapValidationReserveEntity;
import com.solidwall.tartib.entities.GeneratedClassificationEntity;
import com.solidwall.tartib.entities.GeographicBonusEntity;
import com.solidwall.tartib.entities.GridBonusEntity;
import com.solidwall.tartib.entities.GridBonusLevelEntity;
import com.solidwall.tartib.entities.MinisterEntity;
import com.solidwall.tartib.entities.ProjectClassementEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.entities.ResponseAdminEvaluationComponentEntity;
import com.solidwall.tartib.entities.SecteurBonusEntity;
import com.solidwall.tartib.entities.SecteurEntity;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.repositories.AdminEvaluationResponseRepository;
import com.solidwall.tartib.repositories.ClassificationRepository;
import com.solidwall.tartib.repositories.CnapValidationReserveRepository;
import com.solidwall.tartib.repositories.FinancialSourceRepository;
import com.solidwall.tartib.repositories.GeneratedClassificationRepository;
import com.solidwall.tartib.repositories.MinisterRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectPlanRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Log
public class ProjectValidationCnapService {

    private final GeneratedClassificationRepository generatedClassificationRepository;
    private final ProjectIdentityRepository projectIdentityRepository;
    private final ProjectPlanRepository projectPlanRepository;
    private final FinancialSourceRepository financialSourceRepository;
    private final AdminEvaluationResponseRepository adminEvaluationResponseRepository;
    private final MinisterRepository ministerRepository;
    private final ClassificationRepository classificationRepository;
    private final CnapValidationReserveRepository cnapValidationReserveRepository;

    /**
     * Get the latest generated classification with its projects
     */
    public GeneratedClassificationEntity getLatestClassement() {
        log.info("Fetching latest generated classification");

        return generatedClassificationRepository.findTopByOrderByGenerationDateDesc()
                .orElseThrow(() -> new RuntimeException("No generated classification found"));
    }

    /**
     * Get ministries and their projects from the latest classification
     */
    public List<MinistryProjectsDto> getMinistryProjectsFromLatestClassement() {
        log.info("Fetching ministry projects from latest classification");

        GeneratedClassificationEntity latestClassement = getLatestClassement();

        // Group projects by ministry
        Map<String, List<ProjectClassementEntity>> projectsByMinistry = latestClassement.getProjectClassements()
                .stream()
                .filter(pc -> pc.getProjectIdentity().getMinister() != null)
                .collect(Collectors.groupingBy(
                        pc -> pc.getProjectIdentity().getMinister().getName()));

        return projectsByMinistry.entrySet().stream()
                .map(entry -> MinistryProjectsDto.builder()
                        .ministryName(entry.getKey())
                        .projects(entry.getValue().stream()
                                .map(this::mapToProjectValidationDetail)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get project validation data for a specific project
     */
    /**
     * Get project validation data for a specific project
     */
    public ProjectValidationDataDto getProjectValidationData(Long projectId) {
        log.info("Getting validation data for project ID: {}", projectId);

        ProjectIdentityEntity project = projectIdentityRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Get project details with classement data
        ProjectValidationDetailDto projectDetail = mapToProjectValidationDetailWithClassement(project);

        // Calculate budget information
        ProjectBudgetInfo budgetInfo = calculateProjectBudget(projectId);

        // Get score data
        ProjectScoreDataDto scoreData = getProjectScoreData(projectId);

        // Get bonus analysis
        ProjectBonusAnalysisDto bonusAnalysis = analyzeProjectBonuses(project);

        return ProjectValidationDataDto.builder()
                .project(projectDetail)
                .coutTotal(budgetInfo.getCoutTotal())
                .financementInterne(budgetInfo.getFinancementInterne())
                .scoreData(scoreData)
                .bonusAnalysis(bonusAnalysis)
                .build();

    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null || amount.equals(BigDecimal.ZERO)) {
            return "0 TND";
        }
        return String.format("%,.0f TND", amount);
    }

    /**
     * Map project to validation detail with classement data
     */
    private ProjectValidationDetailDto mapToProjectValidationDetailWithClassement(ProjectIdentityEntity project) {
        ProjectValidationDetailDto.ProjectValidationDetailDtoBuilder builder = ProjectValidationDetailDto.builder()
                .id(project.getId())
                .code(project.getCode())
                .intitule(project.getName())
                .ministryName(project.getMinister() != null ? project.getMinister().getName() : "")
                .etat(project.getStatut().toString());
        // Map codePit to code (as requested)

        // Get budget info for cout and finInterieur
        ProjectBudgetInfo budgetInfo = calculateProjectBudget(project.getId());
        builder.cout(formatCurrency(budgetInfo.getCoutTotal()))
                .finInterieur(formatCurrency(budgetInfo.getFinancementInterne()));

        // Try to get classement data for this project
        try {
            GeneratedClassificationEntity latestClassement = getLatestClassement();
            Optional<ProjectClassementEntity> projectClassementOpt = latestClassement.getProjectClassements().stream()
                    .filter(pc -> pc.getProjectIdentity().getId().equals(project.getId()))
                    .findFirst();

            if (projectClassementOpt.isPresent()) {
                ProjectClassementEntity projectClassement = projectClassementOpt.get();
                ClassificationEntity classificationSystem = latestClassement.getClassificationSystem();

                // Add classement data
                builder.scoreGlobal(projectClassement.getInitialScore())
                        .scoreBonifie(  roundDouble(projectClassement.getScoreBonifie(),2))
                        .rang(projectClassement.getRang());

                // Get secteur from classification system
                if (project.getMinister() != null && classificationSystem.getNomenclatureSecteur() != null) {
                    Optional<SecteurEntity> secteurOpt = classificationSystem.getNomenclatureSecteur()
                            .getSecteurs().stream()
                            .filter(secteur -> secteur.getMinisters() != null &&
                                    secteur.getMinisters().contains(project.getMinister()))
                            .findFirst();

                    if (secteurOpt.isPresent()) {
                        builder.secteur(secteurOpt.get().getTitle());
                    }
                }

                // Get category from project data if available

            } else {
                log.warn("Project {} not found in latest classification", project.getCode());
                // Set secteur from project data if available
                if (project.getSector() != null) {
                    builder.secteur(project.getSector().getName());
                }

            }
        } catch (Exception e) {
            log.error("Error retrieving classement data for project {}: {}", project.getCode(), e.getMessage());
            // Fallback to project data
            if (project.getSector() != null) {
                builder.secteur(project.getSector().getName());
            }

        }

        return builder.build();
    }

    /**
     * Calculate project budget and internal financing
     */
    private ProjectBudgetInfo calculateProjectBudget(Long projectId) {
        log.debug("Calculating budget for project ID: {}", projectId);

        // First get the project identity
        Optional<ProjectIdentityEntity> projectOpt = projectIdentityRepository.findById(projectId);
        if (!projectOpt.isPresent()) {
            log.warn("Project not found for ID: {}", projectId);
            return new ProjectBudgetInfo(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        // Then get the project plan using the project entity
        Optional<ProjectPlanEntity> projectPlanOpt = projectPlanRepository.findByProjectIdentity(projectOpt.get());

        if (!projectPlanOpt.isPresent()) {
            log.warn("No project plan found for project ID: {}", projectId);
            return new ProjectBudgetInfo(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        ProjectPlanEntity projectPlan = projectPlanOpt.get();

        // Convert Long to BigDecimal properly
        BigDecimal coutTotal = projectPlan.getCoutTotale() != null ? BigDecimal.valueOf(projectPlan.getCoutTotale())
                : BigDecimal.ZERO;

        // Calculate internal financing from financial sources
        BigDecimal financementInterne = BigDecimal.ZERO;

        if (projectPlan.getFinancialSource() != null) {
            financementInterne = projectPlan.getFinancialSource().stream()
                    .filter(fs -> "Financement interieur".equalsIgnoreCase(fs.getType()))
                    .map(fs -> fs.getMontantDinars() != null ? BigDecimal.valueOf(fs.getMontantDinars())
                            : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        log.debug("Calculated budget - Total: {}, Internal financing: {}", coutTotal, financementInterne);
        return new ProjectBudgetInfo(coutTotal, financementInterne);
    }

    /**
     * Get project score data including averages
     */
    private ProjectScoreDataDto getProjectScoreData(Long projectId) {
        log.debug("Getting score data for project ID: {}", projectId);
        Optional<ProjectIdentityEntity> projectOpt = projectIdentityRepository.findById(projectId);

        ProjectIdentityEntity project = projectOpt.get();
        Long ministryId = project.getMinister() != null ? project.getMinister().getId() : null;
        // Get admin evaluation for this project
        Optional<AdminEvaluationResponseEntity> adminEvalOpt = adminEvaluationResponseRepository
                .findByProjectId(projectId);

        if (!adminEvalOpt.isPresent()) {
            log.warn("No admin evaluation found for project ID: {}", projectId);
            return ProjectScoreDataDto.builder()
                    .projectScores(new double[5]) // Default empty scores
                    .averageScores(new double[5])
                    .averageScoresByMinistry(new double[5]) // Default empty ministry averages
                    .componentNames(getDefaultComponentNames())
                    .build();
        }

        AdminEvaluationResponseEntity adminEval = adminEvalOpt.get();

        // Extract component scores for this project
        double[] projectScores = extractComponentScores(adminEval);

        // Calculate average scores across all projects
        double[] averageScores = calculateAverageScores();
        // Calculate average scores for this ministry
        double[] averageScoresByMinistry = calculateAverageScoresByMinistry(ministryId);

        // Get component names
        String[] componentNames = getComponentNames(adminEval);

        return ProjectScoreDataDto.builder()
                .projectScores(projectScores)
                .averageScores(averageScores)
                .averageScoresByMinistry(averageScoresByMinistry)
                .componentNames(componentNames)
                .build();
    }

    /**
     * Analyze project bonuses - what it got vs what it could have gotten
     */
    private ProjectBonusAnalysisDto analyzeProjectBonuses(ProjectIdentityEntity project) {
        log.debug("Analyzing bonuses for project: {}", project.getCode());

        try {
            // Get the classification system used for this project
            GeneratedClassificationEntity latestClassement = getLatestClassement();
            ClassificationEntity classificationSystem = latestClassement.getClassificationSystem();

            // Find this project's classement data
            Optional<ProjectClassementEntity> projectClassementOpt = latestClassement.getProjectClassements().stream()
                    .filter(pc -> pc.getProjectIdentity().getId().equals(project.getId()))
                    .findFirst();

            if (!projectClassementOpt.isPresent()) {
                log.warn("Project {} not found in latest classification", project.getCode());
                return ProjectBonusAnalysisDto.builder()
                        .sectorialBonuses(new ArrayList<>())
                        .geographicBonuses(new ArrayList<>())
                        .build();
            }

            ProjectClassementEntity projectClassement = projectClassementOpt.get();

            // Analyze each type of bonus with individual error handling
            List<BonusOptionDto> sectorialBonuses = new ArrayList<>();
            List<BonusOptionDto> geographicBonuses = new ArrayList<>();
            List<GridBonusGroupDto> gridBonuses = new ArrayList<>();
            Double gridBonusApplied = 0.0; // Default to 0 if not found

            try {
                sectorialBonuses = analyzeSectorialBonuses(project, classificationSystem, projectClassement);
            } catch (Exception e) {
                log.error("Error analyzing sectorial bonuses for project {}: {}", project.getCode(), e.getMessage());
            }

            try {
                geographicBonuses = analyzeGeographicBonuses(project, classificationSystem, projectClassement);
            } catch (Exception e) {
                log.error("Error analyzing geographic bonuses for project {}: {}", project.getCode(), e.getMessage());
            }

            try {
                gridBonusApplied = projectClassement.getBonusGrille() != null ? projectClassement.getBonusGrille()
                        : 0.0;

            } catch (Exception e) {
                log.error("Error analyzing grid bonuses for project {}: {}", project.getCode(), e.getMessage(), e);
                // Return empty list if grid bonus analysis fails
            }

            return ProjectBonusAnalysisDto.builder()
                    .sectorialBonuses(sectorialBonuses)
                    .geographicBonuses(geographicBonuses)
                    .gridBonusApplied(gridBonusApplied)
                    .build();

        } catch (Exception e) {
            log.error("Error in analyzeProjectBonuses for project {}: {}", project.getCode(), e.getMessage(), e);

            // Return empty analysis if everything fails
            return ProjectBonusAnalysisDto.builder()
                    .sectorialBonuses(new ArrayList<>())
                    .geographicBonuses(new ArrayList<>())
                    .build();
        }
    }

    /**
     * Update project status for CNAP validation
     */
    @Transactional
    public ProjectIdentityEntity updateProjectCNAPStatus(Long projectId, String status) {
        log.info("Updating CNAP status for project ID: {} to status: {}", projectId, status);

        ProjectIdentityEntity project = projectIdentityRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Add new CNAP statuses to ProjectStaut enum if not already present
        switch (status.toLowerCase()) {
            case "valide_par_cnap":
                project.setStatut(ProjectStaut.validé_par_cnap);
                break;
            case "valide_avec_reserve":
                project.setStatut(ProjectStaut.validé_avec_reserve);
                break;
            case "rejete_par_cnap":
                project.setStatut(ProjectStaut.rejeté_par_cnap);
                break;
            default:
                throw new IllegalArgumentException("Invalid CNAP status: " + status);
        }

        return projectIdentityRepository.save(project);
    }

    /**
     * Get projects by CNAP status
     */
    /**
     * Get projects by CNAP status
     */
    public List<ProjectValidationDetailDto> getProjectsByCNAPStatus(String status) {
        log.info("Getting projects with CNAP status: {}", status);

        ProjectStaut projectStatus;
        switch (status.toLowerCase()) {
            case "valide_par_cnap":
                projectStatus = ProjectStaut.validé_par_cnap;
                break;
            case "valide_avec_reserve":
                projectStatus = ProjectStaut.validé_avec_reserve;
                break;
            case "rejete_par_cnap":
                projectStatus = ProjectStaut.rejeté_par_cnap;
                break;
            default:
                throw new IllegalArgumentException("Invalid CNAP status: " + status);
        }

        // FIX: Pass the enum directly, not toString()
        return projectIdentityRepository.findByStatut(projectStatus).stream()
                .map(this::mapToProjectValidationDetailWithClassement)
                .collect(Collectors.toList());
    }

    // Helper methods
    private ProjectValidationDetailDto mapToProjectValidationDetail(ProjectIdentityEntity project) {
        return ProjectValidationDetailDto.builder()
                .id(project.getId())
                .code(project.getCode())
                .intitule(project.getName())
                .ministryName(project.getMinister() != null ? project.getMinister().getName() : "")
                .etat(project.getStatut().toString())
                .build();
    }

    private ProjectValidationDetailDto mapToProjectValidationDetail(ProjectClassementEntity projectClassement) {
        ProjectIdentityEntity project = projectClassement.getProjectIdentity();
        return ProjectValidationDetailDto.builder()
                .id(project.getId())
                .code(project.getCode())
                .intitule(project.getName())
                .ministryName(project.getMinister() != null ? project.getMinister().getName() : "")
                .scoreGlobal(projectClassement.getInitialScore())
                .scoreBonifie(roundDouble(  projectClassement.getScoreBonifie(),2))
                .rang(projectClassement.getRang())
                .etat(project.getStatut().toString())
                .build();
    }

    private double[] extractComponentScores(AdminEvaluationResponseEntity adminEval) {
        if (adminEval.getComponents() == null || adminEval.getComponents().isEmpty()) {
            return new double[5]; // Default empty array
        }

        return adminEval.getComponents().stream()
                .mapToDouble(comp -> comp.getAdminComponentScore() != null ? comp.getAdminComponentScore() : 0.0)
                .toArray();
    }

    private double[] calculateAverageScores() {
        log.debug("Calculating average component scores across all evaluated projects");

        // Get all admin evaluations
        List<AdminEvaluationResponseEntity> allEvaluations = adminEvaluationResponseRepository.findAll();

        if (allEvaluations.isEmpty()) {
            log.warn("No admin evaluations found for average calculation");
            return new double[5]; // Default empty array
        }

        log.debug("Found {} total admin evaluations", allEvaluations.size());

        // Filter out evaluations that don't have complete component data
        List<AdminEvaluationResponseEntity> validEvaluations = allEvaluations.stream()
                .filter(eval -> eval.getComponents() != null && !eval.getComponents().isEmpty())
                .collect(Collectors.toList());

        if (validEvaluations.isEmpty()) {
            log.warn("No valid evaluations with components found");
            return new double[5];
        }

        log.debug("Found {} valid evaluations with components", validEvaluations.size());

        // Log component counts for debugging
        validEvaluations.forEach(eval -> {
            log.debug("Evaluation ID {} has {} components",
                    eval.getId(), eval.getComponents().size());
        });

        // Use a consistent component count - get the most common count
        Map<Integer, Long> componentCountFrequency = validEvaluations.stream()
                .collect(Collectors.groupingBy(
                        eval -> eval.getComponents().size(),
                        Collectors.counting()));

        int mostCommonComponentCount = componentCountFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(5);

        log.debug("Most common component count: {}", mostCommonComponentCount);
        log.debug("Component count distribution: {}", componentCountFrequency);

        // Filter evaluations to only use those with the most common component count
        List<AdminEvaluationResponseEntity> consistentEvaluations = validEvaluations.stream()
                .filter(eval -> eval.getComponents().size() == mostCommonComponentCount)
                .collect(Collectors.toList());

        log.debug("Using {} evaluations with consistent component count of {}",
                consistentEvaluations.size(), mostCommonComponentCount);

        double[] averageScores = new double[mostCommonComponentCount];

        // Calculate average for each component position using component ORDER, not ID
        for (int i = 0; i < mostCommonComponentCount; i++) {
            final int componentIndex = i;

            // Get all component scores at this position (ordered by component order)
            List<Double> componentScores = consistentEvaluations.stream()
                    .map(eval -> {
                        // Sort components by ID or order to ensure consistency
                        List<ResponseAdminEvaluationComponentEntity> sortedComponents = eval.getComponents().stream()
                                .sorted((c1, c2) -> Long.compare(c1.getComponent().getId(), c2.getComponent().getId()))
                                .collect(Collectors.toList());

                        if (sortedComponents.size() > componentIndex) {
                            return sortedComponents.get(componentIndex);
                        }
                        return null;
                    })
                    .filter(comp -> comp != null && comp.getAdminComponentScore() != null)
                    .map(comp -> {
                        log.debug("Component {} (ID: {}, Name: {}) score: {}",
                                componentIndex,
                                comp.getComponent().getId(),
                                comp.getComponent().getName(),
                                comp.getAdminComponentScore());
                        return comp.getAdminComponentScore();
                    })
                    .collect(Collectors.toList());

            // Calculate average
            if (!componentScores.isEmpty()) {
                double sum = componentScores.stream().mapToDouble(Double::doubleValue).sum();
                   double average = sum / componentScores.size();
            averageScores[i] = roundDouble(average, 2);
                log.debug("Component {} average: {} (from {} evaluations)",
                        componentIndex, averageScores[i], componentScores.size());
            } else {
                averageScores[i] = 0.0;
                log.warn("No valid scores found for component {}", componentIndex);
            }
        }

        log.debug("Final calculated average scores: {}", Arrays.toString(averageScores));
        return averageScores;
    }

    /**
     * Calculate average scores for a specific ministry
     */
/**
 * Calculate average scores for a specific ministry
 */
private double[] calculateAverageScoresByMinistry(Long ministryId) {
    log.debug("Calculating average component scores for ministry ID: {}", ministryId);

    if (ministryId == null) {
        log.warn("Ministry ID is null, returning empty array");
        return new double[5];
    }

    // Get all admin evaluations for projects belonging to this ministry
    List<AdminEvaluationResponseEntity> ministryEvaluations = adminEvaluationResponseRepository.findAll()
            .stream()
            .filter(eval -> {
                try {
                    // FIXED: Use eval.getProject() directly instead of eval.getProject().getId()
                    ProjectIdentityEntity project = eval.getProject();
                    if (project != null) {
                        return project.getMinister() != null &&
                               project.getMinister().getId().equals(ministryId);
                    }
                    return false;
                } catch (Exception e) {
                    log.warn("Error checking ministry for evaluation {}: {}", eval.getId(), e.getMessage());
                    return false;
                }
            })
            .collect(Collectors.toList());

    if (ministryEvaluations.isEmpty()) {
        log.warn("No admin evaluations found for ministry ID: {}", ministryId);
        return new double[5]; // Default empty array
    }

    log.debug("Found {} total admin evaluations for ministry {}", ministryEvaluations.size(), ministryId);

    // Filter out evaluations that don't have complete component data
    List<AdminEvaluationResponseEntity> validEvaluations = ministryEvaluations.stream()
            .filter(eval -> eval.getComponents() != null && !eval.getComponents().isEmpty())
            .collect(Collectors.toList());

    if (validEvaluations.isEmpty()) {
        log.warn("No valid evaluations with components found for ministry {}", ministryId);
        return new double[5];
    }

    log.debug("Found {} valid evaluations with components for ministry {}", validEvaluations.size(), ministryId);

    // Use a consistent component count - get the most common count
    Map<Integer, Long> componentCountFrequency = validEvaluations.stream()
            .collect(Collectors.groupingBy(
                    eval -> eval.getComponents().size(),
                    Collectors.counting()));

    int mostCommonComponentCount = componentCountFrequency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(5);

    log.debug("Most common component count for ministry {}: {}", ministryId, mostCommonComponentCount);

    // Filter evaluations to only use those with the most common component count
    List<AdminEvaluationResponseEntity> consistentEvaluations = validEvaluations.stream()
            .filter(eval -> eval.getComponents().size() == mostCommonComponentCount)
            .collect(Collectors.toList());

    log.debug("Using {} evaluations with consistent component count of {} for ministry {}",
            consistentEvaluations.size(), mostCommonComponentCount, ministryId);

    double[] averageScores = new double[mostCommonComponentCount];

    // Calculate average for each component position using component ORDER, not ID
    for (int i = 0; i < mostCommonComponentCount; i++) {
        final int componentIndex = i;

        // Get all component scores at this position (ordered by component order)
        List<Double> componentScores = consistentEvaluations.stream()
                .map(eval -> {
                    // Sort components by ID or order to ensure consistency
                    List<ResponseAdminEvaluationComponentEntity> sortedComponents = eval.getComponents().stream()
                            .sorted((c1, c2) -> Long.compare(c1.getComponent().getId(), c2.getComponent().getId()))
                            .collect(Collectors.toList());

                    if (sortedComponents.size() > componentIndex) {
                        return sortedComponents.get(componentIndex);
                    }
                    return null;
                })
                .filter(comp -> comp != null && comp.getAdminComponentScore() != null)
                .map(comp -> {
                    log.debug("Ministry {} - Component {} (ID: {}, Name: {}) score: {}",
                            ministryId, componentIndex,
                            comp.getComponent().getId(),
                            comp.getComponent().getName(),
                            comp.getAdminComponentScore());
                    return comp.getAdminComponentScore();
                })
                .collect(Collectors.toList());

        // FIXED: Calculate average and apply rounding properly
        if (!componentScores.isEmpty()) {
            double sum = componentScores.stream().mapToDouble(Double::doubleValue).sum();
            double average = sum / componentScores.size();
            averageScores[i] = roundDouble(average, 2); // FIXED: Assign to correct array index
            log.debug("Ministry {} - Component {} average: {} (from {} evaluations)",
                    ministryId, componentIndex, averageScores[i], componentScores.size());
        } else {
            averageScores[i] = 0.0;
            log.warn("Ministry {} - No valid scores found for component {}", ministryId, componentIndex);
        }
    }

    log.debug("Final calculated average scores for ministry {}: {}", ministryId, Arrays.toString(averageScores));
    return averageScores;
}
    private String[] getComponentNames(AdminEvaluationResponseEntity adminEval) {
        if (adminEval.getComponents() == null || adminEval.getComponents().isEmpty()) {
            return getDefaultComponentNames();
        }

        return adminEval.getComponents().stream()
                .map(comp -> comp.getComponent().getName())
                .toArray(String[]::new);
    }

    private String[] getDefaultComponentNames() {
        return new String[] { "Composante 1", "Composante 2", "Composante 3", "Composante 4", "Composante 1" };
    }

    private List<BonusOptionDto> analyzeSectorialBonuses(ProjectIdentityEntity project,
            ClassificationEntity classificationSystem,
            ProjectClassementEntity projectClassement) {
        log.debug("Analyzing sectorial bonuses for project: {} using existing classement data", project.getCode());

        List<BonusOptionDto> bonusOptions = new ArrayList<>();

        if (classificationSystem.getSecteurBonuses() == null) {
            log.warn("No sectorial bonus configuration found in classification system");
            return bonusOptions;
        }

        // Get the actual bonus applied to this project from projectClassement
        Double appliedSectorialBonus = projectClassement.getBonusSecteur();

        // Get all available sectorial bonuses from the classification system
        for (SecteurBonusEntity bonus : classificationSystem.getSecteurBonuses()) {
            // Check if this bonus was applied (match the percentage)
            boolean isApplied = appliedSectorialBonus != null &&
                    Math.abs(appliedSectorialBonus - bonus.getBonusPercentage()) < 0.01;

            BonusOptionDto bonusOption = BonusOptionDto.builder()
                    .name(bonus.getSecteur().getTitle())
                    .percentage(bonus.getBonusPercentage())
                    .description(String.format("Bonus sectoriel pour %s", bonus.getSecteur().getTitle()))
                    .isApplied(isApplied)
                    .category("sectorial")
                    .build();

            bonusOptions.add(bonusOption);
        }

        log.debug("Found {} sectorial bonus options for project {}, applied bonus: {}%",
                bonusOptions.size(), project.getCode(), appliedSectorialBonus);
        return bonusOptions;
    }

    private List<BonusOptionDto> analyzeGeographicBonuses(ProjectIdentityEntity project,
            ClassificationEntity classificationSystem,
            ProjectClassementEntity projectClassement) {
        log.debug("Analyzing geographic bonuses for project: {} using existing classement data", project.getCode());

        List<BonusOptionDto> bonusOptions = new ArrayList<>();

        if (classificationSystem.getGeographicBonuses() == null) {
            log.warn("No geographic bonus configuration found in classification system");
            return bonusOptions;
        }

        // Get the actual bonus applied to this project from projectClassement
        Double appliedGeographicBonus = projectClassement.getBonusCategorie();

        // Get all available geographic bonuses from the classification system
        for (GeographicBonusEntity bonus : classificationSystem.getGeographicBonuses()) {
            // Check if this bonus was applied (match the percentage)
            boolean isApplied = appliedGeographicBonus != null &&
                    Math.abs(appliedGeographicBonus - bonus.getBonusPercentage()) < 0.01;

            BonusOptionDto bonusOption = BonusOptionDto.builder()
                    .name(bonus.getGeographicCategory().getTitle())
                    .percentage(bonus.getBonusPercentage())
                    .description(String.format("Bonus géographique pour %s", bonus.getGeographicCategory().getTitle()))
                    .isApplied(isApplied)
                    .category("geographic")
                    .build();

            bonusOptions.add(bonusOption);
        }

        log.debug("Found {} geographic bonus options for project {}, applied bonus: {}%",
                bonusOptions.size(), project.getCode(), appliedGeographicBonus);
        return bonusOptions;
    }

    /**
     * Get projects with CNAP validation status by ministry
     */
    /**
     * Get projects from latest classement by ministry ID
     */
    public List<ProjectValidationDetailDto> getProjectsByMinistry(Long ministryId) {
        log.info("Getting projects from latest classement for ministry ID: {}", ministryId);

        try {
            // Find the ministry
            Optional<MinisterEntity> ministryOpt = ministerRepository.findById(ministryId);
            if (!ministryOpt.isPresent()) {
                log.warn("Ministry not found with ID: {}", ministryId);
                return new ArrayList<>();
            }

            MinisterEntity ministry = ministryOpt.get();
            log.debug("Found ministry: {}", ministry.getName());

            // Get the latest generated classement
            GeneratedClassificationEntity latestClassement = getLatestClassement();

            // Filter projects by ministry from the latest classement
            List<ProjectValidationDetailDto> projects = latestClassement.getProjectClassements().stream()
                    .filter(pc -> pc.getProjectIdentity().getMinister() != null &&
                            pc.getProjectIdentity().getMinister().getId().equals(ministryId))
                    .map(pc -> mapToProjectValidationDetailWithClassement(pc.getProjectIdentity()))
                    .collect(Collectors.toList());

            log.debug("Found {} projects for ministry {} in latest classement", projects.size(), ministry.getName());
            return projects;

        } catch (Exception e) {
            log.error("Error getting projects for ministry {}: {}", ministryId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Analyze grid bonuses - grouped by criteria/indicator
     */
    private List<GridBonusGroupDto> analyzeGridBonuses(ProjectIdentityEntity project,
            ClassificationEntity classificationSystem,
            ProjectClassementEntity projectClassement) {

        System.out.println(
                "Analyzing grid bonuses for project: " + project.getCode() + " using existing classement data");

        List<GridBonusGroupDto> gridBonusGroups = new ArrayList<>();

        try {
            if (classificationSystem.getGridBonuses() == null) {
                System.out.println("No grid bonus configuration found in classification system");
                return gridBonusGroups;
            }

            // Get the actual grid bonus applied to this project from projectClassement
            Double appliedGridBonus = projectClassement.getBonusGrille();
            System.out.println("Applied grid bonus for project: " + project.getCode() + " = " + appliedGridBonus);

            // Get all available grid bonuses from the classification system
            System.out.println(
                    "Found " + classificationSystem.getGridBonuses().size() + " grid bonuses in classification system");

            for (GridBonusEntity gridBonus : classificationSystem.getGridBonuses()) {
                try {
                    System.out.println(
                            "Processing grid bonus: " + gridBonus.getName() + " (ID: " + gridBonus.getId() + ")");

                    if (gridBonus.getLevels() == null || gridBonus.getLevels().isEmpty()) {
                        System.out.println("Grid bonus " + gridBonus.getName() + " has no levels, skipping");
                        continue;
                    }

                    System.out.println(
                            "Grid bonus " + gridBonus.getName() + " has " + gridBonus.getLevels().size() + " levels");

                    // Determine the type and name of this grid bonus
                    String bonusType;
                    String bonusName;

                    if (gridBonus.getIndicator() != null) {
                        bonusType = "indicateur";
                        bonusName = gridBonus.getIndicator().getName() != null ? gridBonus.getIndicator().getName()
                                : "Indicateur inconnu";
                    } else if (gridBonus.getCriteria() != null) {
                        bonusType = "critere";
                        bonusName = gridBonus.getCriteria().getName() != null ? gridBonus.getCriteria().getName()
                                : "Critère inconnu";
                    } else {
                        bonusType = "autre";
                        bonusName = gridBonus.getName() != null ? gridBonus.getName() : "Bonus inconnu";
                    }

                    System.out.println("Bonus type: " + bonusType + ", name: " + bonusName);

                    // Create bonus options for each level of this grid bonus
                    List<BonusOptionDto> bonusOptions = new ArrayList<>();

                    for (GridBonusLevelEntity level : gridBonus.getLevels()) {
                        try {
                            System.out.println("Processing level " + level.getLevelName() + " for grid bonus "
                                    + gridBonus.getName());

                            // Check if this specific level was applied
                            boolean isApplied = appliedGridBonus != null &&
                                    Math.abs(appliedGridBonus - level.getBonusPercentage()) < 0.01;

                            String levelName = level.getLevelName() != null ? level.getLevelName() : "Niveau inconnu";

                            BonusOptionDto bonusOption = BonusOptionDto.builder()
                                    .name(levelName)
                                    .percentage(level.getBonusPercentage() != null ? level.getBonusPercentage() : 0.0)
                                    .description("Seuil: "
                                            + (level.getPointsThreshold() != null ? level.getPointsThreshold() : "N/A"))
                                    .isApplied(isApplied)
                                    .category("grid")
                                    .build();

                            bonusOptions.add(bonusOption);
                            System.out.println(
                                    "Added bonus option: " + bonusOption.getName() + " - Applied: " + isApplied);

                        } catch (Exception e) {
                            System.out.println("Error processing level " + level.getLevelName() + " for grid bonus "
                                    + gridBonus.getName() + ": " + e.getMessage());
                        }
                    }

                    // Create the group for this grid bonus
                    GridBonusGroupDto gridBonusGroup = GridBonusGroupDto.builder()
                            .type(bonusType)
                            .name(bonusName)
                            .bonusOptions(bonusOptions)
                            .build();

                    gridBonusGroups.add(gridBonusGroup);
                    System.out.println("Added grid bonus group: " + bonusType + " - " + bonusName + " with "
                            + bonusOptions.size() + " options");

                } catch (Exception e) {
                    System.out.println("Error processing grid bonus " + gridBonus.getName() + ": " + e.getMessage());
                }
            }

            System.out.println("Found " + gridBonusGroups.size() + " grid bonus groups for project " + project.getCode()
                    + ", applied bonus: " + appliedGridBonus + "%");

        } catch (Exception e) {
            System.out.println("Error in analyzeGridBonuses for project " + project.getCode() + ": " + e.getMessage());
        }

        return gridBonusGroups;
    } // Inner class for budget calculation



    // rollback status to évalué
  public ProjectIdentityEntity rollbackToEvaluatedStatus(long projectIdentityId) {
        log.info("Rolling back project {} to evaluated status", projectIdentityId);

       Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(projectIdentityId);

       if (project.isPresent()) {
           ProjectIdentityEntity projectEntity = project.get();
            if(projectEntity.getStatut() == ProjectStaut.validé_par_cnap ||
               projectEntity.getStatut() == ProjectStaut.validé_avec_reserve ||
               projectEntity.getStatut() == ProjectStaut.rejeté_par_cnap) {
                
                // search for cnapValidationReserveEntity
           Optional<CnapValidationReserveEntity> reserveEntityOpt = cnapValidationReserveRepository
                   .findByProjectIdentityId(projectIdentityId);
              if (reserveEntityOpt.isPresent()) {
                   CnapValidationReserveEntity reserveEntity = reserveEntityOpt.get();
                   // Reset the reserve to null
                   cnapValidationReserveRepository.delete(reserveEntity); 
               } else {
                   log.warn("No reserve found for project {}", projectIdentityId);
               }

           projectEntity.setStatut(ProjectStaut.évalué);
           return projectIdentityRepository.save(projectEntity);
       }
    else {
           log.warn("Project {} is not in a valid CNAP status for rollback", projectIdentityId);
           throw new NotFoundException("Project is not in a valid CNAP status for rollback");   }
    
    
    }
       else {
           throw new NotFoundException("Project not found");
       }
    }
    
    private static class ProjectBudgetInfo {
        private final BigDecimal coutTotal;
        private final BigDecimal financementInterne;

        public ProjectBudgetInfo(BigDecimal coutTotal, BigDecimal financementInterne) {
            this.coutTotal = coutTotal;
            this.financementInterne = financementInterne;
        }

        public BigDecimal getCoutTotal() {
            return coutTotal;
        }

        public BigDecimal getFinancementInterne() {
            return financementInterne;
        }
    }

 public static double roundDouble(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP); // Or other RoundingMode
        return bd.doubleValue();
    }
}
