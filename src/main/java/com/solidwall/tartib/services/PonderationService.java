package com.solidwall.tartib.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.ponderation.CreateDto;
import com.solidwall.tartib.dto.ponderation.UpdateDto;
import com.solidwall.tartib.entities.PonderationEntity;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.entities.PonderationComponentEntity;
import com.solidwall.tartib.entities.PonderationCriteriaEntity;
import com.solidwall.tartib.implementations.PonderationImplementation;
import com.solidwall.tartib.repositories.PonderationRepository;
import com.solidwall.tartib.repositories.EvaluationGridRepository;
import com.solidwall.tartib.repositories.PonderationComponentRepository;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PonderationService implements PonderationImplementation {

    @Autowired
    private PonderationRepository ponderationRepository;

    @Autowired
    private PonderationComponentRepository componentRepository;

    @Autowired
    private EvaluationGridRepository evaluationGridRepository; // Add this

    @Override
    public List<PonderationEntity> findAll() {
        List<PonderationEntity> ponderations = ponderationRepository.findAll();
        if (!ponderations.isEmpty()) {
            return ponderations;
        } else {
            throw new NotFoundException("No ponderations found");
        }
    }

    @Override
    public PonderationEntity getOne(Long id) {
        return ponderationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ponderation not found"));
    }

    @Override
    @Transactional
    public PonderationEntity create(CreateDto data) {
        log.info("Creating new ponderation");
        // First, get the evaluation grid
        EvaluationGridEntity evaluationGrid = evaluationGridRepository.findById(data.getEvaluationGridId())
                .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
        PonderationEntity ponderation = new PonderationEntity();
        ponderation.setMaxindicateur(data.getMaxindicateur());
        ponderation.setMaxnote(data.getMaxnote());
        ponderation.setIsActive(false);
        ponderation.setTotalPoints(data.getMaxnote() * data.getMaxindicateur());
        ponderation.setEvaluationGrid(evaluationGrid); // Set the relationship
        if (!ponderation.getEvaluationGrid().getId().equals(data.getEvaluationGridId())) {
            EvaluationGridEntity newEvaluationGrid = evaluationGridRepository.findById(data.getEvaluationGridId())
                    .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
            ponderation.setEvaluationGrid(newEvaluationGrid);
        }

        // Handle components and their nested criteria
        if (data.getComponents() != null) {
            log.info("Processing {} components", data.getComponents().size());

            List<PonderationComponentEntity> components = data.getComponents().stream()
                    .map(compDto -> {
                        log.info("Processing component: {}", compDto.getName());

                        PonderationComponentEntity component = new PonderationComponentEntity();
                        component.setName(compDto.getName());
                        component.setNumberOfPoints(compDto.getNumberOfPoints());
                        component.setPercentage(compDto.getPercentage());
                        component.setPonderation(ponderation); // Set bidirectional relationship

                        // Handle criteria
                        if (compDto.getCriterias() != null) {
                            log.info("Mapping {} criteria for component {}",
                                    compDto.getCriterias().size(), compDto.getName());

                            List<PonderationCriteriaEntity> criterias = compDto.getCriterias().stream()
                                    .map(critDto -> {
                                        PonderationCriteriaEntity criteria = new PonderationCriteriaEntity();
                                        criteria.setName(critDto.getName());
                                        criteria.setNumberOfPoints(critDto.getNumberOfPoints());
                                        criteria.setPercentage(critDto.getPercentage());
                                        criteria.setComponent(component); // Set bidirectional relationship
                                        return criteria;
                                    })
                                    .collect(Collectors.toList());
                            component.setPonderationcriterias(criterias); 
                        }
                        return component;
                    })
                    .collect(Collectors.toList());
            ponderation.setPonderationcomponents(components) ;
        }

        return ponderationRepository.save(ponderation);
    }

    @Override
    @Transactional
    public PonderationEntity update(Long id, UpdateDto data) {
        PonderationEntity ponderation = ponderationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ponderation not found"));
        log.info("Updating ponderation with ID: {}", id);

        // Update basic fields
        ponderation.setMaxindicateur(data.getMaxindicateur());
        ponderation.setMaxnote(data.getMaxnote());
        // ponderation.setIsActive(data.getIsActive());
        ponderation.setTotalPoints(data.getMaxnote() * data.getMaxindicateur());

        // Clear existing components to prevent orphaned records
        ponderation.getPonderationcomponents().clear();
        List<PonderationComponentEntity> previousComponents = componentRepository.findByPonderationId(id);
        componentRepository.deleteAll(previousComponents);

        // Update components and their nested criteria
        if (data.getComponents() != null) {
            log.info("Processing {} components for update", data.getComponents().size());

            List<PonderationComponentEntity> components = data.getComponents().stream()
                    .map(compDto -> {
                        log.info("Processing component update: {}", compDto.getName());

                        PonderationComponentEntity component = new PonderationComponentEntity();
                        component.setName(compDto.getName());
                        component.setNumberOfPoints(compDto.getNumberOfPoints());
                        component.setPercentage(compDto.getPercentage());
                        component.setPonderation(ponderation);

                        // Handle criteria
                        if (compDto.getCriterias() != null) {
                            log.info("Mapping {} criteria for component {}",
                                    compDto.getCriterias().size(), compDto.getName());

                            List<PonderationCriteriaEntity> criterias = compDto.getCriterias().stream()
                                    .map(critDto -> {
                                        PonderationCriteriaEntity criteria = new PonderationCriteriaEntity();
                                        criteria.setName(critDto.getName());
                                        criteria.setNumberOfPoints(critDto.getNumberOfPoints());
                                        criteria.setPercentage(critDto.getPercentage());
                                        criteria.setComponent(component);
                                        return criteria;
                                    })
                                    .collect(Collectors.toList());
                            component.setPonderationcriterias(criterias); 
                        }
                        return component;
                    })
                    .collect(Collectors.toList());
            ponderation.setPonderationcomponents(components); 
        }

        return ponderationRepository.save(ponderation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PonderationEntity ponderation = ponderationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ponderation not found"));
        ponderationRepository.delete(ponderation);
    }

    @Override
    public PonderationEntity getByEvaluationGridId(Long gridId) {
        return ponderationRepository.findByEvaluationGridId(gridId)
            .orElseThrow(() -> new NotFoundException("No ponderation found for this grid"));
    }

    @Override
    public PonderationEntity activatePonderation(Long gridId,boolean status){
        PonderationEntity ponderationEntity = this.getByEvaluationGridId(gridId);
        ponderationEntity.setIsActive(status);
        return this.ponderationRepository.save(ponderationEntity);
        
    }
}