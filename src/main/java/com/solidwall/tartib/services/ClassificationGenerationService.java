package com.solidwall.tartib.services;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.adminevaluation.response.AdminEvaluationResponseDto;
import com.solidwall.tartib.dto.classificationGeneration.GenerateClassificationDto;
import com.solidwall.tartib.dto.classificationGeneration.GeneratedClassificationResponseDto;
import com.solidwall.tartib.dto.classificationGeneration.ProjectClassementResponseDto;
import com.solidwall.tartib.entities.AdminEvaluationResponseEntity;
import com.solidwall.tartib.entities.ClassificationEntity;
import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.entities.GeneratedClassificationEntity;
import com.solidwall.tartib.entities.GeographicBonusEntity;
import com.solidwall.tartib.entities.GeographicCategoryEntity;
import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.entities.GridBonusEntity;
import com.solidwall.tartib.entities.GridBonusLevelEntity;
import com.solidwall.tartib.entities.MinisterEntity;
import com.solidwall.tartib.entities.NomenclatureGeographicEntity;
import com.solidwall.tartib.entities.NomenclatureSecteurEntity;
import com.solidwall.tartib.entities.ProjectClassementEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectZoneEntity;
import com.solidwall.tartib.entities.ResponseAdminEvaluationComponentEntity;
import com.solidwall.tartib.entities.ResponseAdminEvaluationCriteriaEntity;
import com.solidwall.tartib.entities.ResponseAdminEvaluationIndicatorEntity;
import com.solidwall.tartib.entities.SecteurBonusEntity;
import com.solidwall.tartib.entities.SecteurEntity;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.implementations.ClassificationGenerationImplementation;
import com.solidwall.tartib.repositories.AdminEvaluationResponseRepository;
import com.solidwall.tartib.repositories.ClassificationRepository;
import com.solidwall.tartib.repositories.GeneratedClassificationRepository;
import com.solidwall.tartib.repositories.ProjectClassementRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectZoneRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ClassificationGenerationService implements ClassificationGenerationImplementation {

    @Autowired
    private GeneratedClassificationRepository generatedClassificationRepository;
    
    @Autowired
    private ProjectClassementRepository projectClassementRepository;
    
    @Autowired
    private ClassificationRepository classificationRepository;
    
    @Autowired
    private ProjectIdentityRepository projectIdentityRepository;
    
    @Autowired
    private ProjectZoneRepository projectZoneRepository;

    @Autowired
    private AdminEvaluationResponseRepository adminEvaluationResponseRepository;

    @Override
    @Transactional
    public GeneratedClassificationResponseDto generateClassification(GenerateClassificationDto data) {
        log.info("Starting classification generation with system ID: {}", data.getClassificationSystemId());
            if (generatedClassificationRepository.existsByName(data.getName())) {
        throw new IllegalArgumentException("A classification with this name already exists");
    }
        // 1. Fetch the chosen classification system
        ClassificationEntity classificationSystem = classificationRepository
            .findById(data.getClassificationSystemId())
            .orElseThrow(() -> new NotFoundException("Classification system not found"));
        
        // 2. Get all evaluated projects
        List<ProjectIdentityEntity> evaluatedProjects = getEvaluatedProjects();
        log.info("Found {} evaluated projects", evaluatedProjects.size());
        
        // 3. Calculate bonuses for each project
        List<ProjectClassementEntity> projectClassements = new ArrayList<>();
        
        for (ProjectIdentityEntity project : evaluatedProjects) {
            try {
                ProjectClassementEntity classement = calculateProjectClassement(project, classificationSystem);
                projectClassements.add(classement);
                log.debug("Calculated classement for project: {} - Score: {}", 
                    project.getCode(), classement.getScoreBonifie());
            } catch (Exception e) {
                log.error("Error calculating classement for project {}: {}", project.getCode(), e.getMessage());
                // Continue with other projects
            }
        }
        
        // 4. Sort by bonified score and assign ranks
        assignRanks(projectClassements);
        
        // 5. Create and save the generated classification
        GeneratedClassificationEntity generatedClassification = new GeneratedClassificationEntity();
        generatedClassification.setName(data.getName());
        generatedClassification.setGenerationDate(new Date()); // Auto-generate current timestamp
        generatedClassification.setDescription(data.getDescription());
        generatedClassification.setClassificationSystem(classificationSystem);
        generatedClassification.setProjectClassements(projectClassements);
        
        // Set back reference
        projectClassements.forEach(pc -> pc.setGeneratedClassification(generatedClassification));
        
        GeneratedClassificationEntity saved = generatedClassificationRepository.save(generatedClassification);
        
        log.info("Generated classification saved with ID: {} and {} projects", 
            saved.getId(), projectClassements.size());
        
        return mapToResponseDto(saved);
    }

    private List<ProjectIdentityEntity> getEvaluatedProjects() {
        // Get all projects with status "évalué" (evaluated)
        return projectIdentityRepository.findAll().stream()
            .filter(project -> ProjectStaut.évalué.equals(project.getStatut()))
            .collect(Collectors.toList());
    }

    private ProjectClassementEntity calculateProjectClassement(ProjectIdentityEntity project, 
                                                              ClassificationEntity classificationSystem) {
        log.debug("Calculating classement for project: {}", project.getCode());
        
        ProjectClassementEntity classement = new ProjectClassementEntity();
        classement.setProjectIdentity(project);
        classement.setProjectCreationDate(project.getCreatedAt());
         String ministry = project.getMinister() != null ? 
        project.getMinister().getName() : null;
    //   classement.setMinistry(ministry);
        List<Long> districts = extractDistrictsFromProject(project);
    // classement.setDistricts(districts);
        // For now, set initial score to 0 - will be calculated from admin evaluation later
   // Get admin evaluation response for this project to calculate initial score
Optional<AdminEvaluationResponseEntity> adminEvaluationOpt = 
    adminEvaluationResponseRepository.findByProjectId(project.getId());

Double initialScore = 0.0;
if (adminEvaluationOpt.isPresent()) {
    AdminEvaluationResponseEntity adminEvaluation = adminEvaluationOpt.get();
    
    // Verify that the evaluation uses the same grid as the classification system
    EvaluationGridEntity evaluationGrid = classificationSystem.getEvaluationGrid();
    if (evaluationGrid != null  ) {
        
        // Sum up all component scores from admin evaluation
        if (adminEvaluation.getComponents() != null) {
            initialScore = adminEvaluation.getAdminGlobalScore() != null ? adminEvaluation.getAdminGlobalScore() : 0.0;
            log.debug("Calculated initial score from admin evaluation for {}: {}", 
                project.getCode(), initialScore);
        }
    } else {
        log.warn("Admin evaluation grid mismatch or missing for project {}", project.getCode());
    }
} else {
    log.warn("No admin evaluation found for project {}", project.getCode());
}
        classement.setInitialScore(initialScore);
        
        // Calculate sectoral bonus
        Double bonusSecteur = calculateSectoralBonus(project, classificationSystem);
        classement.setBonusSecteur(bonusSecteur);
        log.debug("Sectoral bonus for {}: {}%", project.getCode(), bonusSecteur);
        
        // Calculate category bonus
        Double bonusCategorie = calculateCategoryBonus(project, classificationSystem);
        classement.setBonusCategorie(bonusCategorie);
        log.debug("Category bonus for {}: {}%", project.getCode(), bonusCategorie);
        
    
        // Calculate grid bonus using admin evaluation and classification grid bonuses
        Double bonusGrille = calculateGridBonus(project, classificationSystem);
        classement.setBonusGrille(bonusGrille);
        System.out.println("Grid bonus for {}: {}%"+ project.getCode()+ bonusGrille);
        
        // Calculate bonified score: initialScore * (1 + sum of bonuses as decimals)
        Double totalBonusPercentage = (bonusSecteur + bonusCategorie + bonusGrille) / 100.0;
        Double scoreBonifie = initialScore * (1 + totalBonusPercentage);
        classement.setScoreBonifie(scoreBonifie);
        
        log.debug("Final bonified score for {}: {}", project.getCode(), scoreBonifie);
        
        return classement;
    }
  public List<Long> extractDistrictsFromProject(ProjectIdentityEntity project) {
 try {
        Optional<ProjectZoneEntity> projectZoneOpt = projectZoneRepository.findByProjectIdentity(project);
        if (!projectZoneOpt.isPresent()) {
            log.warn("Project {} has no zone defined", project.getCode());
            return new ArrayList<>();
        }
        
        ProjectZoneEntity projectZone = projectZoneOpt.get();
        List<GovernorateEntity> governorates = projectZone.getGovernorates();
        
        if (governorates == null || governorates.isEmpty()) {
            log.warn("Project {} has no governorates defined", project.getCode());
            return new ArrayList<>();
        }
        
        // Extract unique district IDs from governorates
        return governorates.stream()
            .filter(gov -> gov.getDistrict() != null)
            .map(gov -> gov.getDistrict().getId())
            .distinct()
            .collect(Collectors.toList());
            
    } catch (Exception e) {
        log.error("Error extracting districts for project {}: {}", project.getCode(), e.getMessage());
        return new ArrayList<>();
    }
}
    private Double calculateSectoralBonus(ProjectIdentityEntity project, ClassificationEntity classificationSystem) {
        try {
            // 1. Get project's minister
            MinisterEntity projectMinister = project.getMinister();
            if (projectMinister == null) {
                log.warn("Project {} has no minister assigned", project.getCode());
                return 0.0;
            }
            
            // 2. Find which secteur contains this minister in the nomenclature
            NomenclatureSecteurEntity nomenclature = classificationSystem.getNomenclatureSecteur();
            if (nomenclature == null || nomenclature.getSecteurs() == null) {
                log.warn("Classification system has no secteur nomenclature");
                return 0.0;
            }
            
            Optional<SecteurEntity> projectSecteur = nomenclature.getSecteurs().stream()
                .filter(secteur -> secteur.getMinisters() != null && 
                                 secteur.getMinisters().contains(projectMinister))
                .findFirst();
            
            if (!projectSecteur.isPresent()) {
                log.warn("Minister {} not found in any secteur for project {}", 
                    projectMinister.getName(), project.getCode());
                return 0.0;
            }
            
            // 3. Get bonus for this secteur from classification
            if (classificationSystem.getSecteurBonuses() == null) {
                log.warn("Classification system has no secteur bonuses configured");
                return 0.0;
            }
            
            Optional<Double> bonus = classificationSystem.getSecteurBonuses().stream()
                .filter(bonusEntity -> bonusEntity.getSecteur().equals(projectSecteur.get()))
                .map(SecteurBonusEntity::getBonusPercentage)
                .findFirst();
            
            return bonus.orElse(0.0);
            
        } catch (Exception e) {
            log.error("Error calculating sectoral bonus for project {}: {}", project.getCode(), e.getMessage());
            return 0.0;
        }
    }

    private Double calculateCategoryBonus(ProjectIdentityEntity project, ClassificationEntity classificationSystem) {
        try {
            // 1. Get project's zone (contains governorates)
            Optional<ProjectZoneEntity> projectZoneOpt = projectZoneRepository.findByProjectIdentity(project);
            if (!projectZoneOpt.isPresent()) {
                log.warn("Project {} has no zone defined", project.getCode());
                return 0.0;
            }
            
            ProjectZoneEntity projectZone = projectZoneOpt.get();
            List<GovernorateEntity> projectGovernorates = projectZone.getGovernorates();
            if (projectGovernorates == null || projectGovernorates.isEmpty()) {
                log.warn("Project {} has no governorates defined", project.getCode());
                return 0.0;
            }
            
            // 2. Get geographic nomenclature from classification system
            NomenclatureGeographicEntity nomenclature = classificationSystem.getNomenclatureGeographic();
            if (nomenclature == null || nomenclature.getGeographicCategories() == null) {
                log.warn("Classification system has no geographic nomenclature");
                return 0.0;
            }
            
            // 3. Find which geographic categories contain these governorates and collect bonuses
            List<Double> applicableBonuses = new ArrayList<>();
            
            for (GeographicCategoryEntity category : nomenclature.getGeographicCategories()) {
                if (category.getGovernorates() == null) continue;
                
                // Check if any project governorate matches this category
                boolean hasMatchingGovernorate = category.getGovernorates().stream()
                    .anyMatch(projectGovernorates::contains);
                
                if (hasMatchingGovernorate) {
                    // Get bonus for this category from classification system
                    if (classificationSystem.getGeographicBonuses() != null) {
                        classificationSystem.getGeographicBonuses().stream()
                            .filter(bonus -> bonus.getGeographicCategory().equals(category))
                            .map(GeographicBonusEntity::getBonusPercentage)
                            .findFirst()
                            .ifPresent(bonus -> {
                                applicableBonuses.add(bonus);
                                log.debug("Found geographic bonus {}% for category {} and project {}", 
                                    bonus, category.getTitle(), project.getCode());
                            });
                    }
                }
            }
            
            // 4. Return HIGHEST bonus (as per requirements)
            return applicableBonuses.stream()
                .max(Double::compareTo)
                .orElse(0.0);
                
        } catch (Exception e) {
            log.error("Error calculating category bonus for project {}: {}", project.getCode(), e.getMessage());
            return 0.0;
        }
    }

    // start caculate bonus grid
  private Double calculateGridBonus(ProjectIdentityEntity project, ClassificationEntity classificationSystem) {
        try {
            log.debug("Calculating grid bonus for project: {}", project.getCode());
            
            // 1. Get admin evaluation response for this project
            Optional<AdminEvaluationResponseEntity> adminEvaluationOpt = 
                adminEvaluationResponseRepository.findByProjectId(project.getId());
            
            if (!adminEvaluationOpt.isPresent()) {
                log.warn("No admin evaluation found for project {} - grid bonus = 0", project.getCode());
                return 0.0;
            }
            
            AdminEvaluationResponseEntity adminEvaluation = adminEvaluationOpt.get();
            
            // 2. Get grid bonuses configured for this classification system
            if (classificationSystem.getGridBonuses() == null || classificationSystem.getGridBonuses().isEmpty()) {
                log.debug("No grid bonuses configured for classification system - grid bonus = 0");
                return 0.0;
            }
            
            // 3. Calculate total grid bonus by processing each GridBonusEntity
            Double totalGridBonus = 0.0;
            
            for (GridBonusEntity gridBonus : classificationSystem.getGridBonuses()) {
                Double bonusForThisGrid = calculateSingleGridBonus(gridBonus, adminEvaluation, project);
                
                System.out.println(gridBonus.getLevels().size());
                totalGridBonus += bonusForThisGrid;
                System.out.println("demo 06 "+totalGridBonus);
                log.debug("Grid bonus '{}' for project {}: {}%", 
                    gridBonus.getName(), project.getCode(), bonusForThisGrid);
            }
            
            log.debug("Total grid bonus for project {}: {}%", project.getCode(), totalGridBonus);
            return totalGridBonus;
            
        } catch (Exception e) {
            log.error("Error calculating grid bonus for project {}: {}", project.getCode(), e.getMessage());
            return 0.0;
        }
    }

    private Double calculateSingleGridBonus(GridBonusEntity gridBonus, 
                                           AdminEvaluationResponseEntity adminEvaluation,
                                           ProjectIdentityEntity project) {
        try {
            // Determine if this is an indicator or criteria bonus
            if (gridBonus.getIndicator() != null) {
                return calculateIndicatorGridBonus(gridBonus, adminEvaluation, project);
            } else if (gridBonus.getCriteria() != null) {
                return calculateCriteriaGridBonus(gridBonus, adminEvaluation, project);
            } else {
                log.warn("GridBonus {} has neither indicator nor criteria defined", gridBonus.getName());
                return 0.0;
            }
        } catch (Exception e) {
            log.error("Error calculating single grid bonus '{}' for project {}: {}", 
                gridBonus.getName(), project.getCode(), e.getMessage());
            return 0.0;
        }
    }

  private Double calculateIndicatorGridBonus(GridBonusEntity gridBonus, 
                                          AdminEvaluationResponseEntity adminEvaluation,
                                          ProjectIdentityEntity project) {
    Long targetIndicatorId = gridBonus.getIndicator().getId();
    Double adminScore = findAdminIndicatorScore(adminEvaluation, targetIndicatorId);
    
    if (adminScore == null) {
        return 0.0;
    }
    
    // Convert to percentage (assuming max score is 5 from ponderation)
    Double percentageScore = (adminScore / 5.0) * 100;
    
    log.debug("Indicator bonus: adminScore={}, percentage={}%", 
        adminScore, percentageScore);
    
    return findBonusForScore(gridBonus, percentageScore, project);
}

    private Double calculateCriteriaGridBonus(GridBonusEntity gridBonus, 
                                             AdminEvaluationResponseEntity adminEvaluation,
                                             ProjectIdentityEntity project) {
        try {
            Long targetCriteriaId = gridBonus.getCriteria().getId();
    Double rawScore = sumAdminScoresForCriteria(adminEvaluation, targetCriteriaId);
    
    // Convert to percentage (assuming max score is 5)
    Double percentageScore = (rawScore / 5.0) * 100;
    
    log.debug("Criteria grid bonus calculation: raw={}, percentage={}%, bonus=?", 
        rawScore, percentageScore);
    
    return findBonusForScore(gridBonus, percentageScore, project);
            
        } catch (Exception e) {
            log.error("Error calculating criteria grid bonus for project {}: {}", 
                project.getCode(), e.getMessage());
            return 0.0;
        }
    }

    private Double findAdminIndicatorScore(AdminEvaluationResponseEntity adminEvaluation, Long indicatorId) {
        if (adminEvaluation.getComponents() == null) return null;
        
        return adminEvaluation.getComponents().stream()
            .filter(component -> component.getCriteria() != null)
            .flatMap(component -> component.getCriteria().stream())
            .filter(criteria -> criteria.getIndicators() != null)
            .flatMap(criteria -> criteria.getIndicators().stream())
            .filter(indicator -> indicator.getIndicator() != null && 
                               indicator.getIndicator().getId().equals(indicatorId))
            .map(ResponseAdminEvaluationIndicatorEntity::getAdminScore)
            .filter(score -> score != null)
            .findFirst()
            .orElse(null);
    }

    private Double sumAdminScoresForCriteria(AdminEvaluationResponseEntity adminEvaluation, Long criteriaId) {
        if (adminEvaluation.getComponents() == null) return null;
        
        // Find the specific criteria and return its adminCriteriaScore directly
        return adminEvaluation.getComponents().stream()
            .filter(component -> component.getCriteria() != null)
            .flatMap(component -> component.getCriteria().stream())
            .filter(criteria -> criteria.getCriteria() != null && 
                               criteria.getCriteria().getId().equals(criteriaId))
            .map(ResponseAdminEvaluationCriteriaEntity::getAdminCriteriaScore)
            .filter(score -> score != null)
            .findFirst()
            .orElse(0.0);
    }

    private Double findBonusForScore(GridBonusEntity gridBonus, Double score, ProjectIdentityEntity project) {
        if (gridBonus.getLevels() == null || gridBonus.getLevels().isEmpty()) {
            log.debug("No bonus levels configured for grid bonus '{}'", gridBonus.getName());
            return 0.0;
        }
        
        // Sort levels by pointsThreshold in descending order to find the highest applicable threshold
        List<GridBonusLevelEntity> sortedLevels = gridBonus.getLevels().stream()
            .sorted((level1, level2) -> Double.compare(level2.getPointsThreshold(), level1.getPointsThreshold()))
            .collect(Collectors.toList());
        
        // Find the first level where score >= pointsThreshold
        for (GridBonusLevelEntity level : sortedLevels) {
            if (score >= level.getPointsThreshold()) {
                log.debug("Score {} meets threshold {} for level '{}' - applying bonus {}%", 
                    score, level.getPointsThreshold(), level.getLevelName(), level.getBonusPercentage());
                return level.getBonusPercentage();
            }
        }
        
        log.debug("Score {} doesn't meet any threshold for grid bonus '{}'", 
            score, gridBonus.getName());
        return 0.0;
    }
    // end caculate bonus grid

    private void assignRanks(List<ProjectClassementEntity> projectClassements) {
        // Sort by bonified score in descending order
        projectClassements.sort((p1, p2) -> Double.compare(p2.getScoreBonifie(), p1.getScoreBonifie()));
        
        // Assign ranks
        for (int i = 0; i < projectClassements.size(); i++) {
            projectClassements.get(i).setRang(i + 1);
        }
    }

    @Override
    public List<GeneratedClassificationResponseDto> findAll(Map<String, String> filters) {
        List<GeneratedClassificationEntity> entities = generatedClassificationRepository.findAll();
        return entities.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public GeneratedClassificationResponseDto getOne(Long id) {
        GeneratedClassificationEntity entity = generatedClassificationRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Generated classification not found"));
        return mapToResponseDto(entity);
    }

    @Override
    public void delete(Long id) {
        GeneratedClassificationEntity entity = generatedClassificationRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Generated classification not found"));
        generatedClassificationRepository.delete(entity);
    }
 
    @Override
    public List<GeneratedClassificationResponseDto> findByClassificationSystem(Long classificationSystemId) {
        ClassificationEntity system = classificationRepository
            .findById(classificationSystemId)
            .orElseThrow(() -> new NotFoundException("Classification system not found"));
        
        List<GeneratedClassificationEntity> entities = generatedClassificationRepository
            .findByClassificationSystem(system);
        
        return entities.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }
  @Override
    public GeneratedClassificationResponseDto findLatestByClassificationSystem(Long classificationSystemId) {
        ClassificationEntity system = classificationRepository
            .findById(classificationSystemId)
            .orElseThrow(() -> new NotFoundException("Classification system not found"));
        
        Optional<GeneratedClassificationEntity> latestEntity = generatedClassificationRepository
            .findFirstByClassificationSystemOrderByGenerationDateDesc(system);
        
        if (!latestEntity.isPresent()) {
            throw new NotFoundException("No generated classification found for this system");
        }
        
        log.debug("Found latest classification for system {}: {} (generated: {})", 
            classificationSystemId, latestEntity.get().getName(), latestEntity.get().getGenerationDate());
        
        return mapToResponseDto(latestEntity.get());
    }
    private GeneratedClassificationResponseDto mapToResponseDto(GeneratedClassificationEntity entity) {
        GeneratedClassificationResponseDto dto = new GeneratedClassificationResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setGenerationDate(entity.getGenerationDate());
        dto.setDescription(entity.getDescription());
        dto.setClassificationSystemId(entity.getClassificationSystem().getId());
        dto.setClassificationSystemTitle(entity.getClassificationSystem().getTitle());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        if (entity.getProjectClassements() != null) {
            List<ProjectClassementResponseDto> classementDtos = entity.getProjectClassements().stream()
                .map(this::mapProjectClassementToDto)
                .collect(Collectors.toList());
            dto.setProjectClassements(classementDtos);
        }
        
        return dto;
    }

 private ProjectClassementResponseDto mapProjectClassementToDto(ProjectClassementEntity entity) {
        ProjectClassementResponseDto dto = new ProjectClassementResponseDto();
        dto.setId(entity.getId());
        dto.setProjectIdentityId(entity.getProjectIdentity().getId());
        dto.setProjectCode(entity.getProjectIdentity().getCode());
        dto.setProjectName(entity.getProjectIdentity().getName());
        dto.setProjectCreationDate(entity.getProjectCreationDate());
        dto.setInitialScore(entity.getInitialScore());
        dto.setBonusSecteur(entity.getBonusSecteur());
        dto.setBonusCategorie(entity.getBonusCategorie());
        dto.setBonusGrille(entity.getBonusGrille());
        dto.setScoreBonifie(entity.getScoreBonifie());
        dto.setRang(entity.getRang());
        
        // DYNAMIC MINISTRY FETCHING
        String ministry = "N/A";
        try {
            ProjectIdentityEntity project = entity.getProjectIdentity();
            if (project != null && project.getMinister() != null) {
                ministry = project.getMinister().getName();
                log.debug("Retrieved ministry '{}' for project '{}'", ministry, project.getCode());
            } else {
                log.debug("No minister found for project '{}'", project != null ? project.getCode() : "unknown");
            }
        } catch (Exception e) {
            log.warn("Failed to fetch ministry for project {}: {}", 
                entity.getProjectIdentity().getCode(), e.getMessage());
        }
        dto.setMinistry(ministry);
        
        // DYNAMIC DISTRICTS FETCHING
        List<Long> districts = new ArrayList<>();
        try {
            ProjectIdentityEntity project = entity.getProjectIdentity();
            if (project != null) {
                Optional<ProjectZoneEntity> projectZoneOpt = projectZoneRepository.findByProjectIdentity(project);
                if (projectZoneOpt.isPresent()) {
                    ProjectZoneEntity projectZone = projectZoneOpt.get();
                    if (projectZone.getDistricts() != null && !projectZone.getDistricts().isEmpty()) {
                        districts = projectZone.getDistricts().stream()
                            .map(DistrictEntity::getId)
                            .collect(Collectors.toList());
                        log.debug("Retrieved {} districts for project '{}'", districts.size(), project.getCode());
                    } else {
                        log.debug("No districts found in project zone for project '{}'", project.getCode());
                    }
                } else {
                    log.debug("No project zone found for project '{}'", project.getCode());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch districts for project {}: {}", 
                entity.getProjectIdentity().getCode(), e.getMessage());
        }
        dto.setDistricts(districts);
        
        return dto;
    }
}