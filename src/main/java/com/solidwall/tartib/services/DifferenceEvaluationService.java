package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.adminevaluation.response.AdminComponentEvaluationResponseDto;
import com.solidwall.tartib.dto.differenceevaluation.CreateDifferenceEvaluationDto;
import com.solidwall.tartib.dto.differenceevaluation.DifferenceEvaluationResponseDto;
import com.solidwall.tartib.entities.AdminEvaluationResponseEntity;
import com.solidwall.tartib.entities.DifferenceEvaluationResponseEntity;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;
import com.solidwall.tartib.entities.ProjectEvaluationResponseEntity;
import com.solidwall.tartib.entities.ResponseAdminEvaluationComponentEntity;
import com.solidwall.tartib.entities.ResponseAdminEvaluationCriteriaEntity;
import com.solidwall.tartib.entities.ResponseAdminEvaluationIndicatorEntity;
import com.solidwall.tartib.entities.ResponseEvaluationComponentEntity;
import com.solidwall.tartib.entities.ResponseEvaluationCriteriaEntity;
import com.solidwall.tartib.entities.ResponseEvaluationIndicatorEntity;
import com.solidwall.tartib.implementations.DifferenceEvaluationImplementation;
import com.solidwall.tartib.repositories.AdminEvaluationResponseRepository;
import com.solidwall.tartib.repositories.DifferenceEvaluationResponseRepository;
import com.solidwall.tartib.repositories.EvaluationIndicateurRepository;
import com.solidwall.tartib.repositories.ProjectEvaluationResponseRepository;
import com.solidwall.tartib.repositories.ResponseAdminEvaluationComponentRepository;
import com.solidwall.tartib.repositories.ResponseAdminEvaluationCriteriaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DifferenceEvaluationService implements DifferenceEvaluationImplementation {
    
    @Autowired
    private DifferenceEvaluationResponseRepository differenceRepository;
    
    @Autowired
    private ProjectEvaluationResponseRepository projectEvaluationRepository;
    
    @Autowired
    private AdminEvaluationResponseRepository adminEvaluationRepository;
    
    @Autowired
    private EvaluationIndicateurRepository indicatorRepository;
    @Autowired
    private ResponseAdminEvaluationComponentRepository componentRepository;
    @Autowired
    private ResponseAdminEvaluationCriteriaRepository criteriaRepository;

    @Override
    public DifferenceEvaluationResponseDto create(CreateDifferenceEvaluationDto data) {
        // First validate all required entities exist
        ProjectEvaluationResponseEntity projectEvaluation = projectEvaluationRepository
            .findById(data.getProjectEvaluationId())
            .orElseThrow(() -> new NotFoundException("Project evaluation not found"));
            
        AdminEvaluationResponseEntity adminEvaluation = adminEvaluationRepository
            .findById(data.getAdminEvaluationId())
            .orElseThrow(() -> new NotFoundException("Admin evaluation not found"));
            
        EvaluationIndicateurEntity indicator = indicatorRepository
            .findById(data.getIndicatorId())
            .orElseThrow(() -> new NotFoundException("Indicator not found"));

        // Create new difference entity
        DifferenceEvaluationResponseEntity difference = new DifferenceEvaluationResponseEntity();
        difference.setProjectEvaluation(projectEvaluation);
        difference.setAdminEvaluation(adminEvaluation);
        difference.setIndicator(indicator);
        difference.setStatus(data.getStatus());
        difference.setProjectRemarks(data.getProjectRemarks());
        difference.setAdminRemarks(data.getAdminRemarks());
        difference.setProjectResponse(data.getProjectResponse());
        difference.setAdminResponse(data.getAdminResponse());
        difference.setComponentId(data.getComponentId());
        difference.setCriteriaId(data.getCriteriaId());

        return mapToDto(differenceRepository.save(difference));
    }

    @Override
    public DifferenceEvaluationResponseDto update(Long id, CreateDifferenceEvaluationDto data) {
        DifferenceEvaluationResponseEntity difference = differenceRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Difference not found"));

        // Update only mutable fields
        // difference.setStatus(data.getStatus());
        difference.setProjectRemarks(data.getProjectRemarks());
        difference.setAdminRemarks(data.getAdminRemarks());
        
        return mapToDto(differenceRepository.save(difference));
    }

    @Override
    public List<DifferenceEvaluationResponseDto> findByProjectEvaluation(Long projectId) {
        ProjectEvaluationResponseEntity projectEval = projectEvaluationRepository
        .findByProjectId(projectId)
        .orElseThrow(() -> new NotFoundException("Project evaluation not found"));

        return differenceRepository
            .findByProjectEvaluationId(projectEval.getId())
            .orElse(new ArrayList<>())
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<DifferenceEvaluationResponseDto> findByAdminEvaluation(Long adminEvaluationId) {
        return differenceRepository
            .findByAdminEvaluationId(adminEvaluationId)
            .orElse(new ArrayList<>())
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public DifferenceEvaluationResponseDto updateStatus(Long id, String status) {
        DifferenceEvaluationResponseEntity difference = differenceRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Difference not found"));
            
        difference.setStatus(status);
        return mapToDto(differenceRepository.save(difference));
    }

    @Override
    @Transactional
    public List<DifferenceEvaluationResponseDto> recalculateDifferences(Long projectId) {
        log.info("Starting intelligent difference recalculation for project: {}", projectId);
        
        // Get both evaluations
        ProjectEvaluationResponseEntity projectEval = projectEvaluationRepository
            .findByProjectId(projectId)
            .orElseThrow(() -> new NotFoundException("Project evaluation not found"));
            
        AdminEvaluationResponseEntity adminEval = adminEvaluationRepository
            .findByProjectId(projectId)
            .orElseThrow(() -> new NotFoundException("Admin evaluation not found"));
    
        // Get existing differences and create a map for quick lookup
        Map<String, DifferenceEvaluationResponseEntity> existingDifferences = differenceRepository
            .findByProjectEvaluationId(projectEval.getId())
            .orElse(new ArrayList<>())
            .stream()
            .collect(Collectors.toMap(
                diff -> generateDifferenceKey(diff.getIndicator().getId(), diff.getComponentId(), diff.getCriteriaId()),
                Function.identity()
            ));
    
        List<DifferenceEvaluationResponseEntity> newDifferences = new ArrayList<>();
    
        // Compare and create/preserve differences
        adminEval.getComponents().forEach(adminComponent -> {
            ResponseEvaluationComponentEntity projectComponent = findMatchingComponent(
                projectEval.getComponents(), 
                adminComponent.getComponent().getId()
            );
    
            if (projectComponent != null) {
                processComponentDifferences(
                    projectComponent,
                    adminComponent,
                    projectEval,
                    adminEval,
                    existingDifferences,
                    newDifferences
                );
            }
        });
    
        // Any remaining items in existingDifferences are no longer valid and should be deleted
        differenceRepository.deleteAll(
            existingDifferences.values().stream()
                .filter(diff -> !newDifferences.contains(diff))
                .collect(Collectors.toList())
        );
    
        // Save all differences (both preserved and new)
        List<DifferenceEvaluationResponseEntity> savedDifferences = 
            differenceRepository.saveAll(newDifferences);
    
        return savedDifferences.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }


    private String generateDifferenceKey(Long indicatorId, Long componentId, Long criteriaId) {
        return String.format("%d:%d:%d", indicatorId, componentId, criteriaId);
    }

    private void processComponentDifferences(
    ResponseEvaluationComponentEntity projectComponent,
    ResponseAdminEvaluationComponentEntity adminComponent,
    ProjectEvaluationResponseEntity projectEval,
    AdminEvaluationResponseEntity adminEval,
    Map<String, DifferenceEvaluationResponseEntity> existingDifferences,
    List<DifferenceEvaluationResponseEntity> newDifferences
) {
    adminComponent.getCriteria().forEach(adminCriteria -> {
        ResponseEvaluationCriteriaEntity projectCriteria = findMatchingCriteria(
            projectComponent.getCriteria(), 
            adminCriteria.getCriteria().getId()
        );

        if (projectCriteria != null) {
            adminCriteria.getIndicators().forEach(adminIndicator -> {
                ResponseEvaluationIndicatorEntity projectIndicator = findMatchingIndicator(
                    projectCriteria.getIndicators(), 
                    adminIndicator.getIndicator().getId()
                );

                if (projectIndicator != null && !indicatorResponsesMatch(projectIndicator, adminIndicator)) {
                    // Check if this difference already exists
                    String differenceKey = generateDifferenceKey(
                        adminIndicator.getIndicator().getId(),
                        adminComponent.getId(),
                        adminCriteria.getId()
                    );

                    DifferenceEvaluationResponseEntity difference = existingDifferences.get(differenceKey);

                    if (difference != null) {
                        // Update only the response values, preserving status and remarks
                        difference.setProjectResponse(projectIndicator.getSelectedNorme());
                        difference.setAdminResponse(adminIndicator.getAdminSelectedNorme());
                        // Remove from existing differences map since we're keeping it
                        existingDifferences.remove(differenceKey);
                        newDifferences.add(difference);
                    } else {
                        // Create new difference
                        newDifferences.add(createDifference(
                            projectEval,
                            adminEval,
                            projectIndicator,
                            adminIndicator,
                            adminComponent.getId(),
                            adminCriteria.getId()
                        ));
                    }
                }
            });
        }
    });
}
    
      void compareIndicators(
        ResponseEvaluationComponentEntity projectComponent,
        ResponseAdminEvaluationComponentEntity adminComponent,
        ProjectEvaluationResponseEntity projectEval,
        AdminEvaluationResponseEntity adminEval,
        List<DifferenceEvaluationResponseEntity> differences
    ) {
        adminComponent.getCriteria().forEach(adminCriteria -> {
            ResponseEvaluationCriteriaEntity projectCriteria = findMatchingCriteria(
                projectComponent.getCriteria(), 
                adminCriteria.getCriteria().getId()
            );

            if (projectCriteria != null) {
                adminCriteria.getIndicators().forEach(adminIndicator -> {
                    ResponseEvaluationIndicatorEntity projectIndicator = findMatchingIndicator(
                        projectCriteria.getIndicators(), 
                        adminIndicator.getIndicator().getId()
                    );

                    if (projectIndicator != null && !indicatorResponsesMatch(projectIndicator, adminIndicator)) {
                        differences.add(createDifference(
                            projectEval, 
                            adminEval, 
                            projectIndicator, 
                            adminIndicator,
                            adminComponent.getId(),
                            adminCriteria.getId()
                        ));
                    }
                });
            }
        });
    }

    private DifferenceEvaluationResponseEntity createDifference(
        ProjectEvaluationResponseEntity projectEval,
        AdminEvaluationResponseEntity adminEval,
        ResponseEvaluationIndicatorEntity projectIndicator,
        ResponseAdminEvaluationIndicatorEntity adminIndicator,
        Long componentId,
        Long criteriaId
    ) {
        DifferenceEvaluationResponseEntity difference = new DifferenceEvaluationResponseEntity();
        difference.setProjectEvaluation(projectEval);
        difference.setAdminEvaluation(adminEval);
        difference.setIndicator(projectIndicator.getIndicator());
        difference.setStatus("PENDING");
        difference.setProjectResponse(projectIndicator.getSelectedNorme());
        difference.setAdminResponse(adminIndicator.getAdminSelectedNorme());
        difference.setComponentId(componentId);
        difference.setCriteriaId(criteriaId);
        return difference;
    }

    // Helper methods for finding matching entities
    private <T> T findMatchingEntity(List<T> entities, Long id, Function<T, Long> idExtractor) {
        return entities.stream()
            .filter(e -> idExtractor.apply(e).equals(id))
            .findFirst()
            .orElse(null);
    }

    private ResponseEvaluationComponentEntity findMatchingComponent(
        List<ResponseEvaluationComponentEntity> components, 
        Long componentId
    ) {
        return findMatchingEntity(components, componentId, 
            c -> c.getComponent().getId());
    }

    private ResponseEvaluationCriteriaEntity findMatchingCriteria(
        List<ResponseEvaluationCriteriaEntity> criteria, 
        Long criteriaId
    ) {
        return findMatchingEntity(criteria, criteriaId, 
            c -> c.getCriteria().getId());
    }

    private ResponseEvaluationIndicatorEntity findMatchingIndicator(
        List<ResponseEvaluationIndicatorEntity> indicators, 
        Long indicatorId
    ) {
        return findMatchingEntity(indicators, indicatorId, 
            i -> i.getIndicator().getId());
    }

    private boolean indicatorResponsesMatch(
        ResponseEvaluationIndicatorEntity projectIndicator,
        ResponseAdminEvaluationIndicatorEntity adminIndicator
    ) {
        return projectIndicator.getScore().equals(adminIndicator.getAdminScore());
    }

    // Mapping method
    private DifferenceEvaluationResponseDto mapToDto(DifferenceEvaluationResponseEntity entity) {
        DifferenceEvaluationResponseDto dto = new DifferenceEvaluationResponseDto();
        ResponseAdminEvaluationComponentEntity componentEntity = componentRepository.findById(entity.getComponentId()).get();
        ResponseAdminEvaluationCriteriaEntity criteriaEntity = criteriaRepository.findById(entity.getCriteriaId()).get();
        dto.setId(entity.getId());
        dto.setProjectEvaluationId(entity.getProjectEvaluation().getId());
        dto.setAdminEvaluationId(entity.getAdminEvaluation().getId());
        dto.setIndicatorId(entity.getIndicator().getId());
        dto.setIndicatorName(entity.getIndicator().getName());
        dto.setComponentId(entity.getComponentId());
        dto.setComponentName(componentEntity.getComponent().getName());
        dto.setCriteriaId(entity.getCriteriaId());
        dto.setCriteriaName(criteriaEntity.getCriteria().getName());
        dto.setStatus(entity.getStatus());
        dto.setProjectResponse(entity.getProjectResponse());
        dto.setAdminResponse(entity.getAdminResponse());
        dto.setProjectRemarks(entity.getProjectRemarks());
        dto.setAdminRemarks(entity.getAdminRemarks());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    @Override
    public void delete(Long AdminEvaluationId) {
        List<DifferenceEvaluationResponseEntity> difference = differenceRepository
            .findByAdminEvaluationId(AdminEvaluationId)
            .orElseThrow(() -> new NotFoundException("Difference not found"));
            
        differenceRepository.deleteAll(difference);
    
    }

    @Override
    public List<Long> findProjectsWithDifferences() {
        log.info("Finding all projects with evaluation differences");
        
        // Query the difference repository to get unique project IDs
        return differenceRepository.findDistinctProjectIds()
            .orElse(new ArrayList<>());
    }
}