package com.solidwall.tartib.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.evaluationgrid.CreateDto;
import com.solidwall.tartib.dto.evaluationgrid.UpdateDto;
import com.solidwall.tartib.entities.AdmissibilityGridEntity;
import com.solidwall.tartib.entities.EvaluationComponentEntity;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;
import com.solidwall.tartib.entities.EvaluationNormeEntity;
import com.solidwall.tartib.entities.PonderationEntity;
import com.solidwall.tartib.implementations.EvaluationGridImplementation;
import com.solidwall.tartib.repositories.EvaluationComponentRepository;
import com.solidwall.tartib.repositories.EvaluationCritiriaRepository;
import com.solidwall.tartib.repositories.EvaluationGridRepository;
import com.solidwall.tartib.repositories.PonderationRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class EvaluationGridService implements EvaluationGridImplementation {
    
    @Autowired
    private EvaluationGridRepository evaluationGridRepository;
    @Autowired
    private EvaluationComponentRepository componentRepository;;
    @Autowired
    private PonderationRepository ponderationRepository;;
    @Autowired
    private EvaluationCritiriaRepository critiriaRepository;

    @Override
    public List<EvaluationGridEntity> findAll() {
        List<EvaluationGridEntity> grids = evaluationGridRepository.findAll();
        if (grids.isEmpty()) {
            throw new NotFoundException("No evaluation grids found");
        }
        return grids;
    }

    @Override
    public EvaluationGridEntity getOne(Long id) {
        return evaluationGridRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
    }

    @Override
    public EvaluationGridEntity create(CreateDto data) {
        EvaluationGridEntity grid = new EvaluationGridEntity();
        grid.setName(data.getName());
        grid.setDescription(data.getDescription());
        grid.setActive(data.isActive());
        grid.setCode(data.getCode());
        grid.setMaxComponents(data.getMaxComponents());
        grid.setMaxCrtieras(data.getMaxCrtieras());
        grid.setMaxIndicator(data.getMaxIndicator());
        grid.setMaxNormes(data.getMaxNormes());
        grid.setMaxNote(data.getMaxNote());
        grid.setState(data.getState());
 
        return evaluationGridRepository.save(grid);
    }
    @Override
    public EvaluationGridEntity updateGeberalSetting(Long id, UpdateDto data) {
        EvaluationGridEntity grid = evaluationGridRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
            log.info("Updating grid with ID: {}", id);

        // Update grid basic properties
        grid.setName(data.getName());
        grid.setDescription(data.getDescription());
        grid.setActive(data.isActive());
        grid.setCode(data.getCode());
        grid.setMaxComponents(data.getMaxComponents());
        grid.setMaxCrtieras(data.getMaxCrtieras());
        grid.setMaxIndicator(data.getMaxIndicator());
        grid.setMaxNormes(data.getMaxNormes());
        grid.setMaxNote(data.getMaxNote());
        if(grid.getComponents() != null) { grid.setComponents(grid.getComponents());};
        EvaluationGridEntity savedGrid = evaluationGridRepository.save(grid);
        log.info("Grid saved. Verifying saved data...");
        savedGrid.getComponents().forEach(comp -> {
            log.info("Saved component {} has {} criterias", comp.getName(), comp.getCritirias().size());
        });
    
        return savedGrid;
    }
    @Override
    
    public EvaluationGridEntity update(Long id, UpdateDto data) {
        EvaluationGridEntity grid = evaluationGridRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
            log.info("Updating grid with ID: {}", id);

    
        // Clear existing components to prevent orphaned records
        grid.getComponents().clear();
        List<EvaluationComponentEntity> previous_components = componentRepository.findByEvaluationGridId(grid.getId()); // Fetch components for the grid
        componentRepository.deleteAll(previous_components); // Delete existing components    

        // Update components and their nested entities
        if (data.getComponents() != null) {
            List<EvaluationComponentEntity> components = data.getComponents().stream()
                .map(compDto -> {
                    log.info("Processing component: {}", compDto.getName());

                    EvaluationComponentEntity component = new EvaluationComponentEntity();
                    component.setName(compDto.getName());
                    component.setDescription(compDto.getDescription());
                    component.setActive(compDto.isActive());
                    component.setEvaluationGrid(grid); // Set bidirectional relationship

                    // Handle criteria
                    if (compDto.getCritirias() != null) {

                        log.info("Mapping {} criterias for component {}", compDto.getCritirias().size(), compDto.getName());

                        List<EvaluationCritiriaEntity> critirias = compDto.getCritirias().stream()
                            .map(critDto -> {
                                EvaluationCritiriaEntity criteria = new EvaluationCritiriaEntity();
                                criteria.setName(critDto.getName());
                                criteria.setCode(critDto.getCode());
                                criteria.setDescription(critDto.getDescription());
                                criteria.setActive(critDto.isActive());
                                criteria.setEvaluationComponent(component); // Set bidirectional relationship

                                // Handle indicators
                                if (critDto.getIndicateurs() != null) {
                                    log.info("Mapping {} indicateurs for criteria {}", critDto.getIndicateurs().size(), critDto.getName());

                                    List<EvaluationIndicateurEntity> indicateurs = critDto.getIndicateurs().stream()
                                        .map(indDto -> {
                                            log.info("Processing indicateur: {}", indDto.getName());

                                            EvaluationIndicateurEntity indicateur = new EvaluationIndicateurEntity();
                                            indicateur.setName(indDto.getName());
                                            indicateur.setCode(indDto.getCode());
                                            indicateur.setDescription(indDto.getDescription());
                                            indicateur.setActive(indDto.isActive());
                                            indicateur.setEvaluationCritiria(criteria); // Set bidirectional relationship

                                            // Handle norms
                                            if (indDto.getNormes() != null) {
                                                log.info("Mapping {} normes for indicateur {}", indDto.getNormes().size(), indDto.getName());

                                                List<EvaluationNormeEntity> normes = indDto.getNormes().stream()
                                                    .map(normeDto -> {
                                                        log.info("Processing norme: {}", normeDto.getName());

                                                        EvaluationNormeEntity norme = new EvaluationNormeEntity();
                                                        norme.setName(normeDto.getName());
                                                        norme.setActive(normeDto.isActive());
                                                        norme.setNote(normeDto.getNote());
                                                        norme.setEvaluationIndicateur(indicateur); // Set bidirectional relationship
                                                        return norme;
                                                    })
                                                    .collect(Collectors.toList());
                                                indicateur.setNormes(normes);
                                            }
                                            return indicateur;
                                        })
                                        .collect(Collectors.toList());
                                    criteria.setIndicateurs(indicateurs);
                                }
                                return criteria;
                            })
                            .collect(Collectors.toList());
                        component.setCritirias(critirias);
                    }
                    return component;
                })
                .collect(Collectors.toList());

        grid.setComponents(components);
    }
    log.info("Saving grid with {} components", grid.getComponents().size());
    grid.getComponents().forEach(comp -> {
        log.info("Component {} has {} criterias", comp.getName(), comp.getCritirias().size());
        comp.getCritirias().forEach(crit -> {
            log.info("Criteria {} has {} indicateurs", crit.getName(), crit.getIndicateurs().size());
            crit.getIndicateurs().forEach(ind -> {
                log.info("Indicateur {} has {} normes", ind.getName(), ind.getNormes().size());
            });
        });
    });

    EvaluationGridEntity savedGrid = evaluationGridRepository.save(grid);
    log.info("Grid saved. Verifying saved data...");
    savedGrid.getComponents().forEach(comp -> {
        log.info("Saved component {} has {} criterias", comp.getName(), comp.getCritirias().size());
    });
    grid.setState(data.getState());

    return savedGrid;

}
    @Override
    public void delete(Long id) {
        EvaluationGridEntity grid = getOne(id);
        Optional<PonderationEntity> ponderationEntity = ponderationRepository.findByEvaluationGridId(id);
        if(ponderationEntity.isPresent()){
            ponderationRepository.delete(ponderationEntity.get());
        }
        evaluationGridRepository.delete(grid);
    }

    @Override
    public EvaluationGridEntity updateMaxValues(Long id, Map<String, Integer> maxValues) {
        EvaluationGridEntity grid = evaluationGridRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
        
        // Update max values if provided
        if (maxValues.containsKey("maxComponents")) {
            int newMaxComponents = maxValues.get("maxComponents");
            if (newMaxComponents < grid.getComponents().size()) {
                throw new IllegalArgumentException("New max components cannot be less than current components count");
            }
            grid.setMaxComponents(newMaxComponents);
        }
        
        if (maxValues.containsKey("maxCrtieras")) {
            int newMaxCrtieras = maxValues.get("maxCrtieras");
            boolean isValid = grid.getComponents().stream()
                .allMatch(comp -> comp.getCritirias().size() <= newMaxCrtieras);
            if (!isValid) {
                throw new IllegalArgumentException("New max criterias cannot be less than current criterias count in any component");
            }
            grid.setMaxCrtieras(newMaxCrtieras);
        }
        
        if (maxValues.containsKey("maxIndicator")) {
            int newMaxIndicator = maxValues.get("maxIndicator");
            boolean isValid = grid.getComponents().stream()
                .flatMap(comp -> comp.getCritirias().stream())
                .allMatch(crit -> crit.getIndicateurs().size() <= newMaxIndicator);
            if (!isValid) {
                throw new IllegalArgumentException("New max indicators cannot be less than current indicators count in any criteria");
            }
            grid.setMaxIndicator(newMaxIndicator);
        }

        return evaluationGridRepository.save(grid);
    }


    @Override
    public void deleteComponent(Long gridId, Long componentId) {
        EvaluationGridEntity grid = evaluationGridRepository.findById(gridId)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));

        if (grid.getComponents().size() <= 1) {
            throw new IllegalStateException("Cannot delete the last component");
        }

        grid.getComponents().removeIf(component -> component.getId().equals(componentId));
        grid.setMaxComponents(grid.getMaxComponents() - 1);
        
        evaluationGridRepository.save(grid);
    }

    @Override
    public void deleteCriteria(Long gridId, Long componentId, Long criteriaId) {
        EvaluationGridEntity grid = evaluationGridRepository.findById(gridId)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));

        EvaluationComponentEntity component = grid.getComponents().stream()
            .filter(comp -> comp.getId().equals(componentId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Component not found"));

        if (component.getCritirias().size() <= 1) {
            throw new IllegalStateException("Cannot delete the last criteria");
        }

        component.getCritirias().removeIf(criteria -> criteria.getId().equals(criteriaId));
        grid.setMaxCrtieras(grid.getMaxCrtieras() - 1);
        
        evaluationGridRepository.save(grid);
    }

    @Override
    public void deleteIndicateur(Long gridId, Long componentId, Long criteriaId, Long indicateurId) {
        EvaluationGridEntity grid = evaluationGridRepository.findById(gridId)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));

        EvaluationComponentEntity component = grid.getComponents().stream()
            .filter(comp -> comp.getId().equals(componentId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Component not found"));

        EvaluationCritiriaEntity criteria = component.getCritirias().stream()
            .filter(crit -> crit.getId().equals(criteriaId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Criteria not found"));

        if (criteria.getIndicateurs().size() <= 1) {
            throw new IllegalStateException("Cannot delete the last indicateur");
        }

        criteria.getIndicateurs().removeIf(indicateur -> indicateur.getId().equals(indicateurId));
        grid.setMaxIndicator(grid.getMaxIndicator() - 1);
        
        evaluationGridRepository.save(grid);
    }

    public List<EvaluationCritiriaEntity> getAllCriteriaByGridId(Long gridId) {
        log.info("Fetching all criteria for grid ID: {}", gridId);
        
        EvaluationGridEntity grid = evaluationGridRepository.findById(gridId)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found with ID: " + gridId));

        // Collect all criteria from all components
        List<EvaluationCritiriaEntity> allCriteria = grid.getComponents().stream()
            .flatMap(component -> component.getCritirias().stream())
            .collect(Collectors.toList());

        if (allCriteria.isEmpty()) {
            log.warn("No criteria found for grid ID: {}", gridId);
            throw new NotFoundException("No criteria found for the specified grid");
        }

        log.info("Found {} criteria for grid ID: {}", allCriteria.size(), gridId);
        return allCriteria;
    }

    // Add this new method to get all indicators by criteria ID
    public List<EvaluationIndicateurEntity> getAllIndicatorsByCriteriaId(Long criteriaId) {
        log.info("Fetching all indicators for criteria ID: {}", criteriaId);
        
        EvaluationCritiriaEntity criteria = critiriaRepository.findById(criteriaId)
            .orElseThrow(() -> new NotFoundException("Criteria not found with ID: " + criteriaId));

        List<EvaluationIndicateurEntity> indicators = criteria.getIndicateurs();

        if (indicators.isEmpty()) {
            log.warn("No indicators found for criteria ID: {}", criteriaId);
            throw new NotFoundException("No indicators found for the specified criteria");
        }

        log.info("Found {} indicators for criteria ID: {}", indicators.size(), criteriaId);
        return indicators;
    }


    @Override
    public EvaluationGridEntity activateGrid(Long id, Boolean status) {
        // First deactivate all grids
        List<EvaluationGridEntity> updatedGrids = evaluationGridRepository.findAll()
            .stream()
            .map(grid -> {
                grid.setActive(false);
                return grid;
            })
            .collect(Collectors.toList());
        
        evaluationGridRepository.saveAll(updatedGrids);
    
        // Then activate the selected grid
        EvaluationGridEntity activatedGrid = this.getOne(id);
        activatedGrid.setActive(status);
        return evaluationGridRepository.save(activatedGrid);
    }
    

    @Override
    public List<EvaluationGridEntity> desactivateAll() {
        List<EvaluationGridEntity> updatedGrids = evaluationGridRepository.findAll()
            .stream()
            .map(grid -> {
                grid.setActive(false);
                return grid;
            })
            .collect(Collectors.toList());
        
        return evaluationGridRepository.saveAll(updatedGrids);
    }

    @Override
    public EvaluationGridEntity getDefault() {
        return evaluationGridRepository.findByIsActive(true).orElseThrow(() -> new NotFoundException("Default evaluation grid not found"));
    }
 
    @Transactional
    public EvaluationGridEntity duplicate(Long id) {
        // Find the original grid
        EvaluationGridEntity originalGrid = evaluationGridRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
    
        // Create new grid with basic properties
        EvaluationGridEntity duplicateGrid = new EvaluationGridEntity();
        duplicateGrid.setName(originalGrid.getName() + " (Copy)");
        duplicateGrid.setDescription(originalGrid.getDescription());
        duplicateGrid.setCode(originalGrid.getCode() + "-copy");
        duplicateGrid.setMaxComponents(originalGrid.getMaxComponents());
        duplicateGrid.setMaxCrtieras(originalGrid.getMaxCrtieras());
        duplicateGrid.setMaxIndicator(originalGrid.getMaxIndicator());
        duplicateGrid.setMaxNormes(originalGrid.getMaxNormes());
        duplicateGrid.setMaxNote(originalGrid.getMaxNote());
        duplicateGrid.setActive(false); // Set as inactive by default
    
        // Duplicate components and their nested entities
        if (originalGrid.getComponents() != null) {
            List<EvaluationComponentEntity> duplicateComponents = originalGrid.getComponents().stream()
                .map(origComponent -> {
                    EvaluationComponentEntity dupComponent = new EvaluationComponentEntity();
                    dupComponent.setName(origComponent.getName());
                    dupComponent.setDescription(origComponent.getDescription());
                    dupComponent.setActive(origComponent.isActive());
                    dupComponent.setEvaluationGrid(duplicateGrid);
    
                    // Duplicate criteria
                    if (origComponent.getCritirias() != null) {
                        List<EvaluationCritiriaEntity> duplicateCriteria = origComponent.getCritirias().stream()
                            .map(origCriteria -> {
                                EvaluationCritiriaEntity dupCriteria = new EvaluationCritiriaEntity();
                                dupCriteria.setName(origCriteria.getName());
                                dupCriteria.setCode(origCriteria.getCode());
                                dupCriteria.setDescription(origCriteria.getDescription());
                                dupCriteria.setActive(origCriteria.isActive());
                                dupCriteria.setEvaluationComponent(dupComponent);
    
                                // Duplicate indicators
                                if (origCriteria.getIndicateurs() != null) {
                                    List<EvaluationIndicateurEntity> duplicateIndicators = origCriteria.getIndicateurs().stream()
                                        .map(origIndicator -> {
                                            EvaluationIndicateurEntity dupIndicator = new EvaluationIndicateurEntity();
                                            dupIndicator.setName(origIndicator.getName());
                                            dupIndicator.setCode(origIndicator.getCode());
                                            dupIndicator.setDescription(origIndicator.getDescription());
                                            dupIndicator.setActive(origIndicator.isActive());
                                            dupIndicator.setEvaluationCritiria(dupCriteria);
    
                                            // Duplicate norms
                                            if (origIndicator.getNormes() != null) {
                                                List<EvaluationNormeEntity> duplicateNorms = origIndicator.getNormes().stream()
                                                    .map(origNorm -> {
                                                        EvaluationNormeEntity dupNorm = new EvaluationNormeEntity();
                                                        dupNorm.setName(origNorm.getName());
                                                        dupNorm.setNote(origNorm.getNote());
                                                        dupNorm.setActive(origNorm.isActive());
                                                        dupNorm.setEvaluationIndicateur(dupIndicator);
                                                        return dupNorm;
                                                    })
                                                    .collect(Collectors.toList());
                                                dupIndicator.setNormes(duplicateNorms);
                                            }
                                            return dupIndicator;
                                        })
                                        .collect(Collectors.toList());
                                    dupCriteria.setIndicateurs(duplicateIndicators);
                                }
                                return dupCriteria;
                            })
                            .collect(Collectors.toList());
                        dupComponent.setCritirias(duplicateCriteria);
                    }
                    return dupComponent;
                })
                .collect(Collectors.toList());
            duplicateGrid.setComponents(duplicateComponents);
        }
    
        return evaluationGridRepository.save(duplicateGrid);
    }

}
