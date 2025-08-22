package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.implementations.AdminEvaluationImplementation;
import com.solidwall.tartib.implementations.DifferenceEvaluationImplementation;
import com.solidwall.tartib.dto.adminevaluation.request.*;
import com.solidwall.tartib.dto.adminevaluation.response.*;
import com.solidwall.tartib.entities.*;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.repositories.*;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AdminEvaluationService implements AdminEvaluationImplementation {

        @Autowired
        private AdminEvaluationResponseRepository adminEvaluationRepository;

        @Autowired
        private ProjectEvaluationResponseRepository projectEvaluationRepository;

        @Autowired
        private ProjectIdentityRepository projectRepository;

        @Autowired
        private ResponseAdminEvaluationComponentRepository componentRepository;

        @Autowired
        private EvaluationGridRepository gridRepository;

        @Autowired
        private StudyRepository studyRepository;
        @Autowired
        DifferenceEvaluationImplementation differenceEvaluationService;
        @Autowired
        private ProjectStudyService projectStudyService;

/**
 * Calculate the sum of all admin component scores
 */
private Double calculateAdminGlobalScore(AdminEvaluationResponseEntity evaluation) {
    if (evaluation.getComponents() == null || evaluation.getComponents().isEmpty()) {
        return 0.0;
    }
    
    Double totalScore = 0.0;
    
    // Loop through each admin component and sum their admin scores
    for (ResponseAdminEvaluationComponentEntity component : evaluation.getComponents()) {
        if (component.getCriteria() != null) {
            for (ResponseAdminEvaluationCriteriaEntity criteria : component.getCriteria()) {
                if (criteria.getIndicators() != null) {
                    for (ResponseAdminEvaluationIndicatorEntity indicator : criteria.getIndicators()) {
                        Double adminScore = indicator.getAdminScore();
                        if (adminScore != null) {
                            totalScore += adminScore;
                        }
                    }
                }
            }
        }
    }
    
    return totalScore;
}
        @Override
        public AdminEvaluationResponseDto create(CreateAdminEvaluationDto data) {
                // Verify project exists
                ProjectIdentityEntity project = projectRepository.findById(data.getProjectId())
                                .orElseThrow(() -> new NotFoundException("Project not found"));

                // Check if admin evaluation already exists
                if (adminEvaluationRepository.existsByProjectId(data.getProjectId())) {
                        throw new BadRequestException("Admin evaluation already exists for this project");
                }

                // Get original evaluation
                ProjectEvaluationResponseEntity originalEvaluation = projectEvaluationRepository
                                .findByProjectId(data.getProjectId())
                                .orElseThrow(() -> new NotFoundException("Original evaluation not found"));

                // Create main evaluation entity
                AdminEvaluationResponseEntity evaluation = new AdminEvaluationResponseEntity();
                evaluation.setProject(project);
                evaluation.setEvaluationGrid(originalEvaluation.getEvaluationGrid());
                evaluation.setEvaluationDate(data.getEvaluationDate());
                evaluation.setOriginalEvaluation(originalEvaluation);
                evaluation.setAdminGlobalScore(data.getAdminGlobalScore());
                evaluation.setOriginalGlobalScore(data.getOriginalGlobalScore());
                evaluation.setActive(true);
                project.setStatut(ProjectStaut.en_cours_admin_evaluation);

                // Create components
                evaluation.setComponents(
                                data.getComponents().stream()
                                                .map(compDto -> createComponentEntity(compDto, evaluation))
                                                .collect(Collectors.toList()));
        //       differenceEvaluationService.recalculateDifferences(project.getId());

                AdminEvaluationResponseEntity result = adminEvaluationRepository.save(evaluation);
                return mapToDto(result);
        }

        @Override
        @Transactional
        public AdminEvaluationResponseDto update(Long projectId, CreateAdminEvaluationDto data) {
                
            AdminEvaluationResponseEntity evaluation = adminEvaluationRepository.findByProjectId(projectId)
                    .orElseThrow(() -> new NotFoundException("Admin evaluation not found"));
        
            log.info("Updating admin evaluation for project ID: {}", projectId);
            
            // Update project status
            ProjectIdentityEntity project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new NotFoundException("Project not found"));
            project.setStatut(ProjectStaut.en_cours_admin_evaluation);
            projectRepository.save(project);
        
            // Update base properties
            evaluation.setEvaluationDate(data.getEvaluationDate());
            evaluation.setAdminGlobalScore(data.getAdminGlobalScore());
            evaluation.setOriginalGlobalScore(data.getOriginalGlobalScore());
        
            // Create a map of existing components by componentId for easy lookup
            Map<Long, ResponseAdminEvaluationComponentEntity> existingComponents = evaluation.getComponents().stream()
                    .collect(Collectors.toMap(
                        comp -> comp.getComponent().getId(),
                        comp -> comp
                    ));
        
            // Update components
            List<ResponseAdminEvaluationComponentEntity> updatedComponents = new ArrayList<>();
            for (CreateAdminComponentEvaluationDto compDto : data.getComponents()) {
                ResponseAdminEvaluationComponentEntity component = existingComponents.get(compDto.getComponentId());
                if (component == null) {
                    // If component doesn't exist, create new
                    component = createComponentEntity(compDto, evaluation);
                } else {
                    // Update existing component
                    updateComponentEntity(component, compDto);
                }
                updatedComponents.add(component);
            }
        
            evaluation.setComponents(updatedComponents);

//             Double calculatedAdminScore = calculateAdminGlobalScore(evaluation);
//     evaluation.setAdminGlobalScore(calculatedAdminScore);
            AdminEvaluationResponseEntity result = adminEvaluationRepository.save(evaluation);
    differenceEvaluationService.recalculateDifferences(project.getId());
            
            return mapToDto(result);
        }
        @Override
        public AdminEvaluationResponseDto getByProjectId(Long projectId) {
                return mapToDto(adminEvaluationRepository.findByProjectId(projectId)
                                .orElseThrow(() -> new NotFoundException("Admin evaluation not found")));
        }

        @Override
        public void delete(Long projectId) {
                ProjectIdentityEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
                AdminEvaluationResponseEntity evaluation = adminEvaluationRepository.findByProjectId(projectId)
                                .orElseThrow(() -> new NotFoundException("Admin evaluation not found"));
                                // delete diffrence evaluation
                differenceEvaluationService.delete(evaluation.getId());
                                // delete   evaluation centrale

                adminEvaluationRepository.delete(evaluation);
           
                project.setStatut(ProjectStaut.auto_évaluation_validée);
                projectRepository.save(project);
        }

        // Helper methods for entity creation
        private ResponseAdminEvaluationComponentEntity createComponentEntity(
                        CreateAdminComponentEvaluationDto dto,
                        AdminEvaluationResponseEntity evaluation) {

                ResponseAdminEvaluationComponentEntity component = new ResponseAdminEvaluationComponentEntity();
                component.setAdminEvaluationResponse(evaluation);
                component.setComponent(evaluation.getEvaluationGrid().getComponents().stream()
                                .filter(c -> c.getId().equals(dto.getComponentId()))
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException("Component not found in grid")));

                component.setAdminComponentScore(dto.getAdminComponentScore());
                component.setOriginalComponentScore(dto.getOriginalComponentScore());

                // Set original component reference
                component.setOriginalComponentResponse(evaluation.getOriginalEvaluation().getComponents().stream()
                                .filter(c -> c.getId().equals(dto.getOriginalComponentResponseId()))
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException("Original component response not found")));

                component.setCriteria(
                                dto.getCriteria().stream()
                                                .map(critDto -> createCriteriaEntity(critDto, component))
                                                .collect(Collectors.toList()));

                return component;
        }

        private ResponseAdminEvaluationCriteriaEntity createCriteriaEntity(
                        CreateAdminCriteriaEvaluationDto dto,
                        ResponseAdminEvaluationComponentEntity component) {

                ResponseAdminEvaluationCriteriaEntity criteria = new ResponseAdminEvaluationCriteriaEntity();
                criteria.setResponseAdminEvaluationComponent(component);
                criteria.setCriteria(component.getComponent().getCritirias().stream()
                                .filter(c -> c.getId().equals(dto.getCriteriaId()))
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException("Criteria not found in component")));

                criteria.setAdminCriteriaScore(dto.getAdminCriteriaScore());
                criteria.setOriginalCriteriaScore(dto.getOriginalCriteriaScore());

                // Set original criteria reference
                criteria.setOriginalCriteriaResponse(component.getOriginalComponentResponse().getCriteria().stream()
                                .filter(c -> c.getId().equals(dto.getOriginalCriteriaResponseId()))
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException("Original criteria response not found")));

                criteria.setIndicators(
                                dto.getIndicators().stream()
                                                .map(indDto -> createIndicatorEntity(indDto, criteria))
                                                .collect(Collectors.toList()));

                return criteria;
        }

        private ResponseAdminEvaluationIndicatorEntity createIndicatorEntity(
                        CreateAdminIndicatorEvaluationDto dto,
                        ResponseAdminEvaluationCriteriaEntity criteria) {

                ResponseAdminEvaluationIndicatorEntity indicator = new ResponseAdminEvaluationIndicatorEntity();
                indicator.setResponseAdminEvaluationCriteria(criteria);
                indicator.setIndicator(criteria.getCriteria().getIndicateurs().stream()
                                .filter(i -> i.getId().equals(dto.getIndicatorId()))
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException("Indicator not found in criteria")));

                // Set evaluation data
                indicator.setAdminScore(dto.getAdminScore());
                indicator.setAdminWeightedScore(dto.getAdminWeightedScore());
                indicator.setAdminSelectedNorme(dto.getAdminSelectedNorme());
                indicator.setAdminComment(dto.getAdminComment());
                indicator.setAdminJustification(dto.getAdminJustification());

                // Set reference study if provided
                if (dto.getReferenceStudyId() != null) {
  Long projectId = criteria.getResponseAdminEvaluationComponent()
            .getAdminEvaluationResponse().getProject().getId();
            Map<String, String> parameters = new HashMap<>();

            // Add the projectIdentity ID as a String
            parameters.put("projectIdentity", projectId.toString()) ; // Replace 123 with your actual project ID
            
            // Call the method with the parameters map
             
            ProjectStudyEntity projectStudyEntity = projectStudyService.findOne( parameters);
            if (projectStudyEntity == null) {
                throw new NotFoundException("Project study not found for the given project ID");
            }
            StudyForProject study = projectStudyEntity.getStudies().stream()
                    .filter(s -> s.getId().equals(dto.getReferenceStudyId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Reference study not found in project study"));
             indicator.setReferenceStudy(study);

 
                }

                // Set original indicator reference
                indicator.setOriginalIndicatorResponse(
                                criteria.getOriginalCriteriaResponse().getIndicators().stream()
                                                .filter(i -> i.getId().equals(dto.getOriginalIndicatorResponseId()))
                                                .findFirst()
                                                .orElseThrow(() -> new NotFoundException(
                                                                "Original indicator response not found")));

                return indicator;
        }

        // helper methods for updating entities
        private void updateComponentEntity(ResponseAdminEvaluationComponentEntity component, 
                                 CreateAdminComponentEvaluationDto dto) {
    // Update component scores
    component.setAdminComponentScore(dto.getAdminComponentScore());
    component.setOriginalComponentScore(dto.getOriginalComponentScore());

    // Create maps for existing criteria
    Map<Long, ResponseAdminEvaluationCriteriaEntity> existingCriteria = component.getCriteria().stream()
            .collect(Collectors.toMap(
                crit -> crit.getCriteria().getId(),
                crit -> crit
            ));

    // Update criteria
    List<ResponseAdminEvaluationCriteriaEntity> updatedCriteria = new ArrayList<>();
    for (CreateAdminCriteriaEvaluationDto critDto : dto.getCriteria()) {
        ResponseAdminEvaluationCriteriaEntity criteria = existingCriteria.get(critDto.getCriteriaId());
        if (criteria == null) {
            criteria = createCriteriaEntity(critDto, component);
        } else {
            updateCriteriaEntity(criteria, critDto);
        }
        updatedCriteria.add(criteria);
    }

    component.setCriteria(updatedCriteria);
}

private void updateCriteriaEntity(ResponseAdminEvaluationCriteriaEntity criteria, 
                                CreateAdminCriteriaEvaluationDto dto) {
    // Update criteria scores
    criteria.setAdminCriteriaScore(dto.getAdminCriteriaScore());
    criteria.setOriginalCriteriaScore(dto.getOriginalCriteriaScore());

    // Create maps for existing indicators
    Map<Long, ResponseAdminEvaluationIndicatorEntity> existingIndicators = criteria.getIndicators().stream()
            .collect(Collectors.toMap(
                ind -> ind.getIndicator().getId(),
                ind -> ind
            ));

    // Update indicators
    List<ResponseAdminEvaluationIndicatorEntity> updatedIndicators = new ArrayList<>();
    for (CreateAdminIndicatorEvaluationDto indDto : dto.getIndicators()) {
        ResponseAdminEvaluationIndicatorEntity indicator = existingIndicators.get(indDto.getIndicatorId());
        if (indicator == null) {
            indicator = createIndicatorEntity(indDto, criteria);
        } else {
            updateIndicatorEntity(indicator, indDto);
        }
        updatedIndicators.add(indicator);
    }

    criteria.setIndicators(updatedIndicators);
}

private void updateIndicatorEntity(ResponseAdminEvaluationIndicatorEntity indicator, 
                                 CreateAdminIndicatorEvaluationDto dto) {
    indicator.setAdminScore(dto.getAdminScore());
    indicator.setAdminWeightedScore(dto.getAdminWeightedScore());
    indicator.setAdminSelectedNorme(dto.getAdminSelectedNorme());
    indicator.setAdminComment(dto.getAdminComment());
    indicator.setAdminJustification(dto.getAdminJustification());

    if (dto.getReferenceStudyId() != null) {
        Map<String, String> parameters = new HashMap<>();
        Long projectId =  indicator.getResponseAdminEvaluationCriteria().getResponseAdminEvaluationComponent()
        .getAdminEvaluationResponse().getProject().getId();
        // Add the projectIdentity ID as a String
        parameters.put("projectIdentity", projectId.toString()) ; // Replace 123 with your actual project ID
        
        // Call the method with the parameters map
         
        ProjectStudyEntity projectStudyEntity = projectStudyService.findOne( parameters);
        if (projectStudyEntity == null) {
            throw new NotFoundException("Project study not found for the given project ID");
        }
        StudyForProject study = projectStudyEntity.getStudies().stream()
                .filter(s -> s.getId().equals(dto.getReferenceStudyId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reference study not found in project study"));
        indicator.setReferenceStudy(study);

 
    }
}
        // Mapping methods for response DTOs
        private AdminEvaluationResponseDto mapToDto(AdminEvaluationResponseEntity entity) {
                AdminEvaluationResponseDto dto = new AdminEvaluationResponseDto();
                dto.setId(entity.getId());
                dto.setProjectId(entity.getProject().getId());
                dto.setProjectName(entity.getProject().getName());
                dto.setGridId(entity.getEvaluationGrid().getId());
                dto.setEvaluationDate(entity.getEvaluationDate());
                dto.setActive(entity.isActive());
                dto.setAdminGlobalScore(entity.getAdminGlobalScore());
                dto.setOriginalGlobalScore(entity.getOriginalGlobalScore());

                dto.setComponents(entity.getComponents().stream()
                                .map(this::mapComponentToDto)
                                .collect(Collectors.toList()));

                return dto;
        }

        private AdminComponentEvaluationResponseDto mapComponentToDto(ResponseAdminEvaluationComponentEntity entity) {
                AdminComponentEvaluationResponseDto dto = new AdminComponentEvaluationResponseDto();
                dto.setId(entity.getId());
                dto.setComponentId(entity.getComponent().getId());
                dto.setComponentName(entity.getComponent().getName());
                dto.setAdminComponentScore(entity.getAdminComponentScore());
                dto.setOriginalComponentScore(entity.getOriginalComponentScore());

                dto.setCriteria(entity.getCriteria().stream()
                                .map(this::mapCriteriaToDto)
                                .collect(Collectors.toList()));

                return dto;
        }

        private AdminCriteriaEvaluationResponseDto mapCriteriaToDto(ResponseAdminEvaluationCriteriaEntity entity) {
                AdminCriteriaEvaluationResponseDto dto = new AdminCriteriaEvaluationResponseDto();
                dto.setId(entity.getId());
                dto.setCriteriaId(entity.getCriteria().getId());
                dto.setCriteriaName(entity.getCriteria().getName());
                dto.setAdminCriteriaScore(entity.getAdminCriteriaScore());
                dto.setOriginalCriteriaScore(entity.getOriginalCriteriaScore());

                dto.setIndicators(entity.getIndicators().stream()
                                .map(this::mapIndicatorToDto)
                                .collect(Collectors.toList()));

                return dto;
        }

        private AdminIndicatorEvaluationResponseDto mapIndicatorToDto(ResponseAdminEvaluationIndicatorEntity entity) {
                AdminIndicatorEvaluationResponseDto dto = new AdminIndicatorEvaluationResponseDto();
                dto.setId(entity.getId());
                dto.setIndicatorId(entity.getIndicator().getId());
                dto.setIndicatorName(entity.getIndicator().getName());
                dto.setAdminScore(entity.getAdminScore());
                dto.setAdminWeightedScore(entity.getAdminWeightedScore());
                dto.setAdminSelectedNorme(entity.getAdminSelectedNorme());
                dto.setAdminComment(entity.getAdminComment());
                dto.setAdminJustification(entity.getAdminJustification());

                // Map original values
                ResponseEvaluationIndicatorEntity original = entity.getOriginalIndicatorResponse();
                if (original != null) {
                        dto.setOriginalScore(original.getScore());
                        dto.setOriginalComment(original.getComment());
                        dto.setOriginalJustification(original.getJustification());
                }

                if (entity.getReferenceStudy() != null) {
                        dto.setReferenceStudyId(entity.getReferenceStudy().getId());
                        dto.setReferenceStudyName(entity.getReferenceStudy().getName());
                }

                return dto;
        }

        @Override
        public void validateAdminEvaluation(Long projectId) {
                AdminEvaluationResponseEntity adminevaluation = adminEvaluationRepository.findByProjectId(projectId)
                                .orElseThrow(() -> new NotFoundException("Evaluation not found"));

                // Check if all indicators have responses
                boolean isComplete = adminevaluation.getComponents().stream()
                                .flatMap(comp -> comp.getCriteria().stream())
                                .flatMap(crit -> crit.getIndicators().stream())
                                .allMatch(ind -> ind.getAdminScore() != null);

                if (!isComplete) {
                        throw new BadRequestException("All indicators must be evaluated before validation");
                }
                differenceEvaluationService.recalculateDifferences(projectId);

                // Update project status
                ProjectIdentityEntity project = projectRepository.findById(projectId)
                                .orElseThrow(() -> new NotFoundException("Project not found"));
                                project.setStatut(ProjectStaut.en_phase_contradictoire);

                projectRepository.save(project);
        }
        @Override
        public void validateFinalAdminEvaluation(Long projectId) {
                AdminEvaluationResponseEntity adminevaluation = adminEvaluationRepository.findByProjectId(projectId)
                                .orElseThrow(() -> new NotFoundException("Evaluation not found"));

                // Check if all indicators have responses
                boolean isComplete = adminevaluation.getComponents().stream()
                                .flatMap(comp -> comp.getCriteria().stream())
                                .flatMap(crit -> crit.getIndicators().stream())
                                .allMatch(ind -> ind.getAdminScore() != null);

                if (!isComplete) {
                        throw new BadRequestException("All indicators must be evaluated before validation");
                }
            

                // Update project status
                ProjectIdentityEntity project = projectRepository.findById(projectId)
                                .orElseThrow(() -> new NotFoundException("Project not found"));
                                if(project.getStatut() != ProjectStaut.en_phase_contradictoire){
                                        throw new BadRequestException("Project doit passer par la phase contradictoire avant d'être validé");
                                }
                                project.setStatut(ProjectStaut.évalué);
                                projectRepository.save(project);
        }

        @Override
        public AdminEvaluationResponseDto getById(Long projectId) {
                return mapToDto(adminEvaluationRepository.findById(projectId)
                                .orElseThrow(() -> new NotFoundException("Admin evaluation not found")));
        }
}