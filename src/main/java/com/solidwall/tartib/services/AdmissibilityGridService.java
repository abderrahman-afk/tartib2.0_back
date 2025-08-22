package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.admissibilitygrid.CreateDto;
import com.solidwall.tartib.dto.admissibilitygrid.UpdateDto;
import com.solidwall.tartib.entities.AdmissibilityCriteriaEntity;
import com.solidwall.tartib.entities.AdmissibilityGridEntity;
import com.solidwall.tartib.implementations.AdmissibilityGridImplementation;
import com.solidwall.tartib.repositories.AdmissibilityCritiriaRepository;
import com.solidwall.tartib.repositories.AdmissibilityGridRepository;
import com.solidwall.tartib.repositories.AdmissibilityResponseRepository;

import jakarta.transaction.Transactional;

@Service
public class AdmissibilityGridService implements AdmissibilityGridImplementation {

    @Autowired
    AdmissibilityCritiriaRepository admissibilityCritiriaRepository;

    @Autowired
    AdmissibilityGridRepository admissibilityGridRepository;
    @Autowired
    AdmissibilityResponseRepository responseGridRepository;

    @Override
    public List<AdmissibilityGridEntity> findAll() {
        if (!admissibilityGridRepository.findAll().isEmpty()) {
            return admissibilityGridRepository.findAll();
        } else {
            throw new NotFoundException("not exist any admissibility grid");
        }
    }

    @Override
    public AdmissibilityGridEntity findOne(Map<String, String> data) {
        if (data.get("active") != null) {
            Boolean active = Boolean.valueOf(data.get("active"));
            Optional<AdmissibilityGridEntity> admissibilityGrid = admissibilityGridRepository.findByIsActive(active);
            if (!admissibilityGrid.isPresent())
                throw new NotFoundException("admissibility Grid not found");
            return admissibilityGrid.get();
        }
        throw new BadRequestException("param not exist");

    }

    @Override
    public AdmissibilityGridEntity getOne(Long id) {
        Optional<AdmissibilityGridEntity> admissibilityGrid = admissibilityGridRepository.findById(id);
        if (admissibilityGrid.isPresent()) {
            return admissibilityGrid.get();
        } else {
            throw new NotFoundException("admissibility Grid not exist");
        }
    }

    @Override
    public AdmissibilityGridEntity create(CreateDto data) {
        Optional<AdmissibilityGridEntity> admissibiltygridcheck = admissibilityGridRepository
                .findByName(data.getName());
        if (admissibiltygridcheck.isPresent()) {
            throw new FoundException("there is allready a grid with this name ");
        } else {
            AdmissibilityGridEntity admissibilityGridEntity = new AdmissibilityGridEntity();
            admissibilityGridEntity.setDescription(data.getDescription());
            admissibilityGridEntity.setName(data.getName());
            
            if (data.getCritirias() != null) {

                admissibilityCritiriaRepository.deleteByAdmissibiltyGridId(admissibilityGridEntity.getId());
                List<AdmissibilityCriteriaEntity> critirias = data.getCritirias().stream()
                        .map(critiriaDto -> {
                            AdmissibilityCriteriaEntity critiria = new AdmissibilityCriteriaEntity();
                            critiria.setName(critiriaDto.getName());
                            critiria.setDescription(critiriaDto.getDescription());
                            critiria.setRequirementLevel(critiriaDto.getRequirementLevel());
                            critiria.setActive(true);
                            critiria.setAdmissibiltyGrid(admissibilityGridEntity);
                            return critiria;
                        })
                        .collect(Collectors.toList());
                admissibilityGridEntity.setCritirias(critirias);
            }

            return admissibilityGridRepository.save(admissibilityGridEntity);
        }

    }
    private boolean isGridInUse(Long gridId) {
        // Using count query to check if any responses reference this grid
        return responseGridRepository.countByAdmissibilityGridId(gridId) > 0;
    }
    @Override
    @Transactional
    public AdmissibilityGridEntity update(Long id, UpdateDto data) {
        Optional<AdmissibilityGridEntity> admissibiltygrid = admissibilityGridRepository.findById(id);
        if (!admissibiltygrid.isPresent()) {
            throw new NotFoundException("there is no a grid with this id ");
        }
        
        AdmissibilityGridEntity existingGrid = admissibiltygrid.get();
                    return createNewVersion(existingGrid, data);

        // Check if this grid is in use by any responses
        // boolean isGridInUse = isGridInUse(existingGrid.getId());
        
        // if (isGridInUse) {
            // If grid is in use, create a new version
            // return createNewVersion(existingGrid, data);
        // } else {
        //     // Direct update if not in use
        //     return updateExistingGrid(existingGrid, data);
        // }
    }
    private AdmissibilityGridEntity createNewVersion(AdmissibilityGridEntity existingGrid, UpdateDto data) {
        // Create a new grid version
        AdmissibilityGridEntity newVersion = new AdmissibilityGridEntity();
        newVersion.setName(data.getName());
        newVersion.setDescription(data.getDescription());
        newVersion.setVersion(existingGrid.getVersion() + 1);
        newVersion.setTemplate(true);
        newVersion.setActive(existingGrid.isActive());
        
        // Save the grid first to get an ID
        AdmissibilityGridEntity   SavednewVersion = admissibilityGridRepository.save(newVersion);
        
        // Create criteria for the new version
        if (data.getCritirias() != null) {
            List<AdmissibilityCriteriaEntity> critirias = data.getCritirias().stream()
                    .map(critiriaDto -> {
                        AdmissibilityCriteriaEntity critiria = new AdmissibilityCriteriaEntity();
                        critiria.setName(critiriaDto.getName());
                        critiria.setDescription(critiriaDto.getDescription());
                        critiria.setRequirementLevel(critiriaDto.getRequirementLevel());
                        critiria.setActive(critiriaDto.isActive());
                        critiria.setAdmissibiltyGrid(SavednewVersion);
                        return critiria;
                    })
                    .collect(Collectors.toList());
            
            // Save the criteria
            critirias = admissibilityCritiriaRepository.saveAll(critirias);
            SavednewVersion.setCritirias(critirias);
        }
        
        // If this is active, deactivate the old version
        if (existingGrid.isActive()) {
            existingGrid.setActive(false);
            admissibilityGridRepository.save(existingGrid);
        }
        
        return admissibilityGridRepository.save(SavednewVersion);
    }
    private AdmissibilityGridEntity updateExistingGrid(AdmissibilityGridEntity grid, UpdateDto data) {
        // Update basic properties
        grid.setDescription(data.getDescription());
        grid.setName(data.getName());
    
        // Check if this grid is being referenced before deleting criteria
        if (isGridInUse(grid.getId())) {
            throw new BadRequestException("Cannot update grid that is in use by projects");
        }
    
        // Update criteria safely
        if (data.getCritirias() != null) {
            // Delete old criteria only if safe
            admissibilityCritiriaRepository.deleteByAdmissibiltyGridId(grid.getId());
            
            List<AdmissibilityCriteriaEntity> critirias = data.getCritirias().stream()
                    .map(critiriaDto -> {
                        AdmissibilityCriteriaEntity critiria = new AdmissibilityCriteriaEntity();
                        critiria.setName(critiriaDto.getName());
                        critiria.setDescription(critiriaDto.getDescription());
                        critiria.setRequirementLevel(critiriaDto.getRequirementLevel());
                        critiria.setActive(critiriaDto.isActive());
                        critiria.setAdmissibiltyGrid(grid);
                        return critiria;
                    })
                    .collect(Collectors.toList());
            grid.setCritirias(critirias);
        }
        
        return admissibilityGridRepository.save(grid);
    }
    @Override
    public void delete(Long id) {
        Optional<AdmissibilityGridEntity> admissibilityGrid = admissibilityGridRepository.findById(id);
        if (admissibilityGrid.isPresent()) {
            // Check if grid is in use before deletion
            if (isGridInUse(id)) {
                throw new BadRequestException("Cannot delete grid that is in use by projects");
            }
            admissibilityGridRepository.deleteById(id);
        } else {
            throw new NotFoundException("admissibility Grid not exist");
        }
    }

    @Override
    public AdmissibilityGridEntity activateGrid(Long id, Boolean status) {
        admissibilityGridRepository.findAll().forEach(grid -> {
            grid.setActive(false);
            admissibilityGridRepository.save(grid);
        });

        AdmissibilityGridEntity activatedGrid = this.getOne(id);
        activatedGrid.setActive(status);
        return admissibilityGridRepository.save(activatedGrid);

    }

    @Override
    public List<AdmissibilityGridEntity> desactivateAll() {
        admissibilityGridRepository.findAll().forEach(grid -> {
            grid.setActive(false);
            admissibilityGridRepository.save(grid);
        });
        return admissibilityGridRepository.findAll();
    }

}
