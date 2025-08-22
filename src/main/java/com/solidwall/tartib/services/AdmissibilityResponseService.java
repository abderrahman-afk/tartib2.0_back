package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.responseadmissibilitygrid.CreateDto;
import com.solidwall.tartib.dto.responseadmissibilitygrid.UpdateDto;
import com.solidwall.tartib.entities.*;
import com.solidwall.tartib.enums.DerogationStatus;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.implementations.AdmissibilityResponseImplementation;
import com.solidwall.tartib.repositories.*;

import jakarta.transaction.Transactional;

@Service
public class AdmissibilityResponseService implements AdmissibilityResponseImplementation {

    @Autowired
    private AdmissibilityResponseGridRepository responseGridRepository;
    
    @Autowired
    private AdmissibilityResponseCriteriaRepository responseCriteriaRepository;
    
    @Autowired
    private ProjectIdentityRepository projectRepository;
    
    @Autowired
    private AdmissibilityGridRepository gridRepository;
    
    @Autowired
    private AdmissibilityCritiriaRepository criteriaRepository;

    @Override
    public List<AdmissibilityResponseGridEntity> findAll() {
        return responseGridRepository.findAll();
    }

    @Override
    public AdmissibilityResponseGridEntity findOne(Map<String, String> data) {
        if (data.get("projectId") != null) {
            Long projectId = Long.valueOf(data.get("projectId"));
            
            // If "latest" parameter is set to true, return only the latest response
            if (data.get("latest") != null && data.get("latest").equalsIgnoreCase("true")) {
                return responseGridRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("No response found for this project"));
            }
            
            // Original behavior - return a specific response (which might cause the multiple results error)
            return responseGridRepository.findByProjectId(projectId)
                .orElseThrow(() -> new NotFoundException("No response found for this project"));
        }
        throw new BadRequestException("Invalid parameters");
    }

    @Override
    public AdmissibilityResponseGridEntity getOne(Long id) {
        return responseGridRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Response grid not found"));
    }
    private int getPointsForLevel(String level) {
        if (level == null) return 0;
        switch(level.toLowerCase()) {
            case "oui": return 2;
            case "encours": return 1;
            default: return 0; // "non" or any other value
        }
    }
    private Map<Long, Long> criteriaIdMapping = new HashMap<>();

    private AdmissibilityGridEntity cloneGrid(AdmissibilityGridEntity original) {
        // Create new grid with same properties
        AdmissibilityGridEntity clone = new AdmissibilityGridEntity();
        clone.setName(original.getName() + " (v" + original.getVersion() + ")");
        clone.setDescription(original.getDescription());
        clone.setTemplate(false);  // This is a project-specific clone
        clone.setActive(false);
        clone.setVersion(original.getVersion());
        
        // Save the grid to get an ID
        clone = gridRepository.save(clone);
        
        // Clone criteria
        List<AdmissibilityCriteriaEntity> clonedCriteria = new ArrayList<>();
        for (AdmissibilityCriteriaEntity criteria : original.getCritirias()) {
            AdmissibilityCriteriaEntity clonedCrit = new AdmissibilityCriteriaEntity();
            clonedCrit.setName(criteria.getName());
            clonedCrit.setDescription(criteria.getDescription());
            clonedCrit.setRequirementLevel(criteria.getRequirementLevel());
            clonedCrit.setActive(criteria.isActive());
            clonedCrit.setAdmissibiltyGrid(clone);
            
            // Save cloned criteria
            clonedCrit = criteriaRepository.save(clonedCrit);
            
            // Store mapping from original ID to cloned ID
            criteriaIdMapping.put(criteria.getId(), clonedCrit.getId());
            
            clonedCriteria.add(clonedCrit);
        }
        
        clone.setCritirias(clonedCriteria);
        return clone;
    }
    @Override
    public Integer getLatestGridVersion() {
        Optional<AdmissibilityGridEntity> latestGrid = gridRepository.findByIsActiveAndIsTemplate(true, true);
        return latestGrid.map(AdmissibilityGridEntity::getVersion).orElse(1);
    }

    @Override
    @Transactional
    public AdmissibilityResponseGridEntity create(CreateDto data) {
        try {
            // Reset the mapping for this operation
            criteriaIdMapping.clear();
            
            ProjectIdentityEntity project = projectRepository.findById(data.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));
                
            AdmissibilityGridEntity templateGrid = gridRepository.findByIsActiveAndIsTemplate(true, true)
                .orElseThrow(() -> new NotFoundException("No active admissibility grid found"));
    
            // Clone the grid for this specific project evaluation
            AdmissibilityGridEntity clonedGrid = cloneGrid(templateGrid);
    
            // Create response grid
            AdmissibilityResponseGridEntity responseGrid = new AdmissibilityResponseGridEntity();
            responseGrid.setProject(project);
            responseGrid.setGrid(clonedGrid);
            responseGrid.setStatus("PENDING");
            
            // Save grid first
            AdmissibilityResponseGridEntity savedResponseGrid = responseGridRepository.save(responseGrid);
            
            // Process responses with mapped criteria IDs
            List<AdmissibilityResponseCriteriaEntity> responses = data.getResponses().stream()
                .map(criteriaDto -> {
                    AdmissibilityResponseCriteriaEntity response = new AdmissibilityResponseCriteriaEntity();
                    response.setResponseGrid(savedResponseGrid);
                    
                    // Map the original criteria ID to the cloned one
                    Long originalId = criteriaDto.getCriteriaId();
                    Long clonedId = criteriaIdMapping.get(originalId);
                    
                    if (clonedId == null) {
                        throw new RuntimeException("Failed to find cloned criteria for original ID: " + originalId);
                    }
                    
                    AdmissibilityCriteriaEntity criteria = criteriaRepository.findById(clonedId)
                        .orElseThrow(() -> new NotFoundException("Criteria not found"));
                    
                    response.setCriteria(criteria);
                    response.setResponse(criteriaDto.getResponse());
                    response.setComment(criteriaDto.getComment());
                    
                    // Simple match check
                    if (!criteria.isActive()) {
                        response.setMatches(true); // Skip inactive criteria
                    } else {
                        int requirementPoints = getPointsForLevel(criteria.getRequirementLevel());
                        int responsePoints = getPointsForLevel(criteriaDto.getResponse());
                        response.setMatches(responsePoints >= requirementPoints);
                    }
                    
                    return response;
                })
                .toList();
            
            // Save responses
            List<AdmissibilityResponseCriteriaEntity> savedResponses = responseCriteriaRepository.saveAll(responses);
            savedResponseGrid.setResponses(savedResponses);
                
             

        // ✅ Check if all responses match
        boolean isAdmissible = savedResponses.stream().allMatch(AdmissibilityResponseCriteriaEntity::isMatches);
        savedResponseGrid.setAdmissible(isAdmissible);
        savedResponseGrid.setStatus(isAdmissible ? DerogationStatus.ACCEPTED.toString() : DerogationStatus.PENDING.toString());

        // ✅ Update project status
        ProjectIdentityEntity projectIdentityEntity = savedResponseGrid.getProject();
        projectIdentityEntity.setStatut(isAdmissible ? ProjectStaut.eligible : ProjectStaut.non_eligible);
        projectRepository.save(projectIdentityEntity);

        return responseGridRepository.save(savedResponseGrid); // ✅ Return savedResponseGrid
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
}

    @Override
    public AdmissibilityResponseGridEntity reevaluate(Long projectId) {
        // Find the existing response
        AdmissibilityResponseGridEntity existingResponse = responseGridRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
        .stream()
    .findFirst() // Take
            .orElseThrow(() -> new NotFoundException("No existing evaluation found"));
        
        // Get the latest active template grid
        AdmissibilityGridEntity latestGrid = gridRepository.findByIsActiveAndIsTemplate(true, true)
        
            .orElseThrow(() -> new NotFoundException("No active admissibility grid found"));
        
        // Check if we're already using the latest grid version
        // if (existingResponse.getGrid().getParentGrid() != null && 
        //     existingResponse.getGrid().getParentGrid().getId().equals(latestGrid.getId())) {
        //     // Just update the existing responses
        //     return existingResponse;
        // }
        
        // Clone the latest grid
        AdmissibilityGridEntity clonedGrid = cloneGrid(latestGrid);
        
        // Create a new response with the latest grid
        AdmissibilityResponseGridEntity newResponse = new AdmissibilityResponseGridEntity();
        newResponse.setProject(existingResponse.getProject());
        newResponse.setGrid(clonedGrid);
        newResponse.setStatus("PENDING");
        
        // Initialize with blank responses
        List<AdmissibilityResponseCriteriaEntity> newCriteriaResponses = new ArrayList<>();
        for (AdmissibilityCriteriaEntity criteria : clonedGrid.getCritirias()) {
            AdmissibilityResponseCriteriaEntity response = new AdmissibilityResponseCriteriaEntity();
            response.setResponseGrid(newResponse);
            response.setCriteria(criteria);
            response.setResponse(""); // Empty initial response
            response.setComment("");
            response.setMatches(false);
            newCriteriaResponses.add(response);
        }
        
        newResponse.setResponses(newCriteriaResponses);
        return responseGridRepository.save(newResponse);
    }
    @Override
    public AdmissibilityResponseGridEntity update(Long id, UpdateDto data) {
        AdmissibilityResponseGridEntity responseGrid = getOne(id);
        
        // Delete old responses
        responseCriteriaRepository.deleteAll(responseGrid.getResponses());
        
        // Create new responses
        List<AdmissibilityResponseCriteriaEntity> responses = data.getResponses().stream()
            .map(criteriaDto -> {
                AdmissibilityResponseCriteriaEntity response = new AdmissibilityResponseCriteriaEntity();
                response.setResponseGrid(responseGrid);
                
                AdmissibilityCriteriaEntity criteria = criteriaRepository.findById(criteriaDto.getCriteriaId())
                    .orElseThrow(() -> new NotFoundException("Criteria not found"));
                
                response.setCriteria(criteria);
                response.setResponse(criteriaDto.getResponse());
                response.setComment(criteriaDto.getComment());
                response.setMatches(criteriaDto.getResponse().equalsIgnoreCase(criteria.getRequirementLevel()));
                
                return response;
            })
            .toList();
        
        List<AdmissibilityResponseCriteriaEntity> savedResponses = responseCriteriaRepository.saveAll(responses);
        responseGrid.setResponses(savedResponses);
        
        // Check if all responses match
        boolean isAdmissible = savedResponses.stream().allMatch(AdmissibilityResponseCriteriaEntity::isMatches);
        responseGrid.setAdmissible(isAdmissible);
        responseGrid.setStatus(isAdmissible ? DerogationStatus.ACCEPTED.toString() : DerogationStatus.PENDING.toString());
        if(responseGrid.isAdmissible()==true)
   {     ProjectIdentityEntity projectIdentityEntity = responseGrid.getProject();
    projectIdentityEntity.setStatut(ProjectStaut.eligible);
    projectRepository.save(projectIdentityEntity);
    }     else if(responseGrid.isAdmissible()!=true)
    {     ProjectIdentityEntity projectIdentityEntity = responseGrid.getProject();
        projectIdentityEntity.setStatut(ProjectStaut.non_eligible);
        projectRepository.save(projectIdentityEntity);
     }
        return responseGridRepository.save(responseGrid);
    }

    @Override
    public void delete(Long id) {
        responseGridRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AdmissibilityResponseGridEntity verifyAdmissibility(Long id) {
        AdmissibilityResponseGridEntity responseGrid = getOne(id);
        
        // Verify each response
        for (AdmissibilityResponseCriteriaEntity response : responseGrid.getResponses()) {
            response.setMatches(
                response.getResponse().equalsIgnoreCase(response.getCriteria().getRequirementLevel())
            );
        }
        
        // Update grid status
        boolean isAdmissible = responseGrid.getResponses().stream()
            .allMatch(AdmissibilityResponseCriteriaEntity::isMatches);
            
        responseGrid.setAdmissible(isAdmissible);

        
        responseGrid.setStatus(isAdmissible ? DerogationStatus.ACCEPTED.toString() : DerogationStatus.PENDING.toString());
     
        return responseGridRepository.save(responseGrid);
    }

    @Override
    public AdmissibilityResponseGridEntity submitDerogation(Long id, String derogationText) {
        AdmissibilityResponseGridEntity responseGrid = getOne(id);
        responseGrid.setDerogationText(derogationText);
        responseGrid.setStatus(DerogationStatus.PENDING_DEROGATION.toString());
        return responseGridRepository.save(responseGrid);
    }
}