package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.projectevaluation.request.CreateComponentEvaluationDto;
import com.solidwall.tartib.dto.projectevaluation.request.CreateCriteriaEvaluationDto;
import com.solidwall.tartib.dto.projectevaluation.request.CreateIndicatorEvaluationDto;
import com.solidwall.tartib.dto.projectevaluation.request.CreateProjectEvaluationDto;
import com.solidwall.tartib.dto.projectevaluation.response.ComponentEvaluationResponseDto;
import com.solidwall.tartib.dto.projectevaluation.response.CriteriaEvaluationResponseDto;
import com.solidwall.tartib.dto.projectevaluation.response.IndicatorEvaluationResponseDto;
import com.solidwall.tartib.dto.projectevaluation.response.ProjectEvaluationResponseDto;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.entities.PonderationEntity;
import com.solidwall.tartib.entities.ProjectEvaluationResponseEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectStudyEntity;
import com.solidwall.tartib.entities.ResponseEvaluationComponentEntity;
import com.solidwall.tartib.entities.ResponseEvaluationCriteriaEntity;
import com.solidwall.tartib.entities.ResponseEvaluationIndicatorEntity;
import com.solidwall.tartib.entities.StudyEntity;
import com.solidwall.tartib.entities.StudyForProject;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.implementations.ProjectEvaluationImplementation;
import com.solidwall.tartib.repositories.EvaluationGridRepository;
import com.solidwall.tartib.repositories.PonderationRepository;
import com.solidwall.tartib.repositories.ProjectEvaluationResponseRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ResponseEvaluationComponentRepository;
import com.solidwall.tartib.repositories.StudyRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProjectEvaluationService implements ProjectEvaluationImplementation {
    @Autowired
    private ProjectEvaluationResponseRepository evaluationRepository;

    @Autowired
    private ProjectIdentityRepository projectRepository;

    @Autowired
    private EvaluationGridRepository gridRepository;

    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private PonderationRepository PonderationRepository;
    @Autowired
    private ResponseEvaluationComponentRepository componentRepository;
    @Autowired
    private ProjectStudyService projectStudyService;

    @Override
    public ProjectEvaluationResponseDto create(CreateProjectEvaluationDto data) {
        // Check if evaluation already exists
        if (evaluationRepository.existsByProjectId(data.getProjectId())) {
            throw new BadRequestException("Project evaluation already exists");
        }

        // Get required entities
        ProjectIdentityEntity project = projectRepository.findById(data.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));

        EvaluationGridEntity grid = gridRepository.findById(data.getGridId())
                .orElseThrow(() -> new NotFoundException("Grid not found"));

        // Create main evaluation entity
        ProjectEvaluationResponseEntity evaluation = new ProjectEvaluationResponseEntity();
        evaluation.setProject(project);
        evaluation.setEvaluationGrid(grid);
        evaluation.setEvaluationDate(data.getEvaluationDate());
        evaluation.setActive(true);
        project.setStatut(ProjectStaut.en_cours_auto_evaluation);
        projectRepository.save(project);
        // Create components
        evaluation.setComponents(
                data.getComponents().stream()
                        .map(compDto -> createComponentEntity(compDto, evaluation))
                        .collect(Collectors.toList()));

        // Save and return
        return mapToDto(evaluationRepository.save(evaluation));
    }

    @Override
    @Transactional
    public ProjectEvaluationResponseDto update(Long projectId, CreateProjectEvaluationDto data) {
        // Get existing evaluation and validate project
        ProjectEvaluationResponseEntity evaluation = evaluationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new NotFoundException("Project evaluation not found"));
        
        ProjectIdentityEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        project.setStatut(ProjectStaut.en_cours_auto_evaluation);
        projectRepository.save(project);
    
        // Update basic evaluation properties
        evaluation.setEvaluationDate(data.getEvaluationDate());
    
        // Create lookup map for existing components
        Map<Long, ResponseEvaluationComponentEntity> existingComponents = evaluation.getComponents().stream()
                .collect(Collectors.toMap(
                    comp -> comp.getComponent().getId(),
                    comp -> comp
                ));
    
        // Process each component in the update data
        List<ResponseEvaluationComponentEntity> updatedComponents = new ArrayList<>();
        for (CreateComponentEvaluationDto compDto : data.getComponents()) {
            ResponseEvaluationComponentEntity component = existingComponents.get(compDto.getComponentId());
            if (component == null) {
                // Create new component if it doesn't exist
                component = createComponentEntity(compDto, evaluation);
            } else {
                // Update existing component
                updateComponentEntity(component, compDto);
            }
            updatedComponents.add(component);
        }
    
        evaluation.setComponents(updatedComponents);
        return mapToDto(evaluationRepository.save(evaluation));
    }
    @Override
    public ProjectEvaluationResponseDto getByProjectId(Long projectId) {
        return mapToDto(evaluationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new NotFoundException("Project evaluation not found")));
    }

    @Override
    public void delete(Long projectId) {
        ProjectEvaluationResponseEntity evaluation = evaluationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new NotFoundException("Project evaluation not found"));

        evaluationRepository.delete(evaluation);
        ProjectIdentityEntity project = projectRepository.findById(projectId).get();
        project.setStatut(ProjectStaut.eligible);
        projectRepository.save(project);

    }
// helper for creating entities 

    private ResponseEvaluationComponentEntity createComponentEntity(CreateComponentEvaluationDto dto,
            ProjectEvaluationResponseEntity evaluation) {
        ResponseEvaluationComponentEntity component = new ResponseEvaluationComponentEntity();
        component.setProjectEvaluationResponse(evaluation);
        component.setComponent(evaluation.getEvaluationGrid().getComponents().stream()
                .filter(c -> c.getId().equals(dto.getComponentId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Component not found in grid")));

        component.setCriteria(
                dto.getCriteria().stream()
                        .map(critDto -> createCriteriaEntity(critDto, component))
                        .collect(Collectors.toList()));

        return component;
    }

    private ResponseEvaluationCriteriaEntity createCriteriaEntity(CreateCriteriaEvaluationDto dto,
            ResponseEvaluationComponentEntity component) {
        ResponseEvaluationCriteriaEntity criteria = new ResponseEvaluationCriteriaEntity();
        criteria.setResponseEvaluationComponent(component);
        criteria.setCriteria(component.getComponent().getCritirias().stream()
                .filter(c -> c.getId().equals(dto.getCriteriaId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Criteria not found in component")));

        criteria.setIndicators(
                dto.getIndicators().stream()
                        .map(indDto -> createIndicatorEntity(indDto, criteria))
                        .collect(Collectors.toList()));

        return criteria;
    }

    private ResponseEvaluationIndicatorEntity createIndicatorEntity(CreateIndicatorEvaluationDto dto,
            ResponseEvaluationCriteriaEntity criteria) {
        ResponseEvaluationIndicatorEntity indicator = new ResponseEvaluationIndicatorEntity();
        indicator.setResponseEvaluationCriteria(criteria);
        indicator.setIndicator(criteria.getCriteria().getIndicateurs().stream()
                .filter(i -> i.getId().equals(dto.getIndicatorId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Indicator not found in criteria")));

        // Set evaluation data
        indicator.setScore(dto.getScore());
        indicator.setWheightedScore(dto.getWheightedScore());
        indicator.setSelectedNorme(dto.getSelectedNorme());

        indicator.setComment(dto.getComment());
        indicator.setJustification(dto.getJustification());

        // Set reference study if provided
        if (dto.getReferenceStudyId() != null) {
            Long projectId = criteria.getResponseEvaluationComponent()
            .getProjectEvaluationResponse().getProject().getId();
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

        return indicator;
    }
// helper for updating entities 
private void updateComponentEntity(ResponseEvaluationComponentEntity component, 
                                 CreateComponentEvaluationDto dto) {
    // Create lookup map for existing criteria
    Map<Long, ResponseEvaluationCriteriaEntity> existingCriteria = component.getCriteria().stream()
            .collect(Collectors.toMap(
                crit -> crit.getCriteria().getId(),
                crit -> crit
            ));

    // Update criteria
    List<ResponseEvaluationCriteriaEntity> updatedCriteria = new ArrayList<>();
    for (CreateCriteriaEvaluationDto critDto : dto.getCriteria()) {
        ResponseEvaluationCriteriaEntity criteria = existingCriteria.get(critDto.getCriteriaId());
        if (criteria == null) {
            criteria = createCriteriaEntity(critDto, component);
        } else {
            updateCriteriaEntity(criteria, critDto);
        }
        updatedCriteria.add(criteria);
    }

    component.setCriteria(updatedCriteria);
}

private void updateCriteriaEntity(ResponseEvaluationCriteriaEntity criteria, 
                                CreateCriteriaEvaluationDto dto) {
    // Create lookup map for existing indicators
    Map<Long, ResponseEvaluationIndicatorEntity> existingIndicators = criteria.getIndicators().stream()
            .collect(Collectors.toMap(
                ind -> ind.getIndicator().getId(),
                ind -> ind
            ));

    // Update indicators
    List<ResponseEvaluationIndicatorEntity> updatedIndicators = new ArrayList<>();
    for (CreateIndicatorEvaluationDto indDto : dto.getIndicators()) {
        ResponseEvaluationIndicatorEntity indicator = existingIndicators.get(indDto.getIndicatorId());
        if (indicator == null) {
            indicator = createIndicatorEntity(indDto, criteria);
        } else {
            updateIndicatorEntity(indicator, indDto);
        }
        updatedIndicators.add(indicator);
    }

    criteria.setIndicators(updatedIndicators);
}

private void updateIndicatorEntity(ResponseEvaluationIndicatorEntity indicator, 
                                 CreateIndicatorEvaluationDto dto) {
    // Update indicator evaluation data
    indicator.setScore(dto.getScore());
    indicator.setWheightedScore(dto.getWheightedScore());
    indicator.setSelectedNorme(dto.getSelectedNorme());
    indicator.setComment(dto.getComment());
    indicator.setJustification(dto.getJustification());

    // Update reference study if provided
    if (dto.getReferenceStudyId() != null) {
        Map<String, String> parameters = new HashMap<>();
        Long projectId =  indicator.getResponseEvaluationCriteria().getResponseEvaluationComponent()
        .getProjectEvaluationResponse().getProject().getId();
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
    private ProjectEvaluationResponseDto mapToDto(ProjectEvaluationResponseEntity entity) {
        ProjectEvaluationResponseDto dto = new ProjectEvaluationResponseDto();
        dto.setId(entity.getId());
        dto.setProjectId(entity.getProject().getId());
        dto.setProjectName(entity.getProject().getName());
        dto.setGridId(entity.getEvaluationGrid().getId());
        dto.setEvaluationDate(entity.getEvaluationDate());
        dto.setActive(entity.isActive());

        dto.setComponents(entity.getComponents().stream()
                .map(this::mapComponentToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private ComponentEvaluationResponseDto mapComponentToDto(ResponseEvaluationComponentEntity entity) {
        ComponentEvaluationResponseDto dto = new ComponentEvaluationResponseDto();
        dto.setId(entity.getId());
        dto.setComponentId(entity.getComponent().getId());
        dto.setComponentName(entity.getComponent().getName());

        dto.setCriteria(entity.getCriteria().stream()
                .map(this::mapCriteriaToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private CriteriaEvaluationResponseDto mapCriteriaToDto(ResponseEvaluationCriteriaEntity entity) {
        CriteriaEvaluationResponseDto dto = new CriteriaEvaluationResponseDto();
        dto.setId(entity.getId());
        dto.setCriteriaId(entity.getCriteria().getId());
        dto.setCriteriaName(entity.getCriteria().getName());

        dto.setIndicators(entity.getIndicators().stream()
                .map(this::mapIndicatorToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private IndicatorEvaluationResponseDto mapIndicatorToDto(ResponseEvaluationIndicatorEntity entity) {
        IndicatorEvaluationResponseDto dto = new IndicatorEvaluationResponseDto();
        dto.setId(entity.getId());
        dto.setIndicatorId(entity.getIndicator().getId());
        dto.setIndicatorName(entity.getIndicator().getName());
        dto.setScore(entity.getScore());
        dto.setWheightedScore(entity.getWheightedScore());
        dto.setSelectedNorme(entity.getSelectedNorme());

        dto.setComment(entity.getComment());
        dto.setJustification(entity.getJustification());

        if (entity.getReferenceStudy() != null) {
            dto.setReferenceStudyId(entity.getReferenceStudy().getId());
            dto.setReferenceStudyName(entity.getReferenceStudy().getName());
        }

        return dto;
    }

    @Override
    public PonderationEntity checkForPonderation(Long evaluationId) {
        PonderationEntity ponderation = new PonderationEntity();
        if (PonderationRepository.findByEvaluationGridId(evaluationId).isPresent()) {
            ponderation = PonderationRepository.findByEvaluationGridId(evaluationId).get();
        } else {
            throw new NotFoundException("Ponderation not found");
        }
        return ponderation;
    }

    @Override
    public void validateAutoEvaluation(Long projectId) {
        ProjectEvaluationResponseEntity evaluation = evaluationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new NotFoundException("Evaluation not found"));

        // Check if all indicators have responses
        boolean isComplete = evaluation.getComponents().stream()
                .flatMap(comp -> comp.getCriteria().stream())
                .flatMap(crit -> crit.getIndicators().stream())
                .allMatch(ind -> ind.getScore() != null);

        if (!isComplete) {
            throw new BadRequestException("All indicators must be evaluated before validation");
        }

        // Update project status
        ProjectIdentityEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        project.setStatut(ProjectStaut.auto_évalué);
        projectRepository.save(project);
    }
    @Override
    public void validateFinalAutoEvaluation(Long projectId) {
        ProjectEvaluationResponseEntity evaluation = evaluationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new NotFoundException("Evaluation not found"));

 
        // Update project status
        ProjectIdentityEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
                if(project.getStatut() != ProjectStaut.auto_évalué) {
                    throw new BadRequestException("Project is not in a valid state for final validation");
                }
        // Update project status to "auto_evaluation_validée"

        project.setStatut(ProjectStaut.auto_évaluation_validée);
        projectRepository.save(project);
    }
}
