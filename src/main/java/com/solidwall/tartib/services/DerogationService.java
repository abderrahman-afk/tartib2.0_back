package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.derrogation.CreateDto;
import com.solidwall.tartib.dto.derrogation.UpdateDto;
import com.solidwall.tartib.entities.AdmissibilityGridEntity;
import com.solidwall.tartib.entities.DerogationEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.enums.DerogationStatus;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.implementations.DerogationImplementation;
import com.solidwall.tartib.repositories.AdmissibilityGridRepository;
import com.solidwall.tartib.repositories.DerogationRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;

import jakarta.transaction.Transactional;

@Service
public class DerogationService implements DerogationImplementation {

    @Autowired
    private DerogationRepository derogationRepository;
    
    @Autowired
    private ProjectIdentityRepository projectIdentityRepository;
    
    @Autowired
    private AdmissibilityGridRepository admissibilityGridRepository;

    @Override
    public List<DerogationEntity> findAll() {
        return derogationRepository.findAll();
    }

    @Override
    public List<DerogationEntity> findByStatus(DerogationStatus status) {
        return derogationRepository.findByStatus(status);
    }

    @Override
    public DerogationEntity findOne(Map<String, String> data) {
        if (data.get("projectIdentityId") != null && data.get("gridId") != null) {
            return derogationRepository.findByProjectIdentityIdAndAdmissibilityGridId(
                    Long.valueOf(data.get("projectIdentityId")),
                    Long.valueOf(data.get("gridId")))
                .orElseThrow(() -> new NotFoundException("Derogation not found"));
        }
        throw new BadRequestException("Invalid parameters");
    }

    @Override
    public DerogationEntity getOne(Long id) {
        return derogationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Derogation not found"));
    }

    @Override
    @Transactional
    public DerogationEntity create(CreateDto data) {
        // Validate project identity exists
        ProjectIdentityEntity projectIdentity = projectIdentityRepository
            .findById(data.getProjectIdentityId())
            .orElseThrow(() -> new NotFoundException("Project identity not found"));
            
        // Validate admissibility grid exists
        AdmissibilityGridEntity grid = admissibilityGridRepository
            .findById(data.getAdmissibilityGridId())
            .orElseThrow(() -> new NotFoundException("Admissibility grid not found"));

        // Check if derogation already exists for this project and grid
        // derogationRepository.findByProjectIdentityIdAndAdmissibilityGridId(
        //         data.getProjectIdentityId(), 
        //         data.getAdmissibilityGridId())
        //     .ifPresent(existing -> {
        //         throw new BadRequestException("Derogation already exists for this project and grid");
        //     });

        DerogationEntity derogation = new DerogationEntity();
        derogation.setProjectIdentity(projectIdentity);
        derogation.setAdmissibilityGrid(grid);
        derogation.setJustification(data.getJustification());
        derogation.setRequestedBy(data.getRequestedBy());
        derogation.setStatus(DerogationStatus.PENDING);

        return derogationRepository.save(derogation);
    }

    @Override
    @Transactional
    public DerogationEntity update(Long id, UpdateDto data) {
        DerogationEntity derogation = getOne(id);
        
        if (DerogationStatus.PENDING.equals(derogation.getStatus())) {
            derogation.setJustification(data.getJustification());
            
            // Only update status and decidedBy if they're changing from PENDING
            if (data.getStatus() != DerogationStatus.PENDING) {
                derogation.setStatus(data.getStatus());
            }
            
            return derogationRepository.save(derogation);
        } else {
            throw new BadRequestException("Cannot update derogation that is not in PENDING status");
        }
    }

    @Override
    @Transactional
    public DerogationEntity acceptDerogation(Long id, String decidedBy) {
        DerogationEntity derogation = getOne(id);
        
        if (DerogationStatus.PENDING.equals(derogation.getStatus())) {
            // Update derogation status
            derogation.setStatus(DerogationStatus.ACCEPTED);
            derogation.setDecidedBy(decidedBy);
            
            // Update project status
            ProjectIdentityEntity project = derogation.getProjectIdentity();
            project.setStatut(ProjectStaut.eligible);
            projectIdentityRepository.save(project);
            
            // Save and return updated derogation
            return derogationRepository.save(derogation);
        } else {
            throw new BadRequestException("Can only accept derogations in PENDING status");
        }
    }

    @Override
    @Transactional
    public DerogationEntity rejectDerogation(Long id, String decidedBy) {
        DerogationEntity derogation = getOne(id);
        
        if (DerogationStatus.PENDING.equals(derogation.getStatus())) {
            derogation.setStatus(DerogationStatus.REJECTED);
            derogation.setDecidedBy(decidedBy);
            return derogationRepository.save(derogation);
        } else {
            throw new BadRequestException("Can only reject derogations in PENDING status");
        }
    }

    @Override
    public void delete(Long id) {
        DerogationEntity derogation = getOne(id);
        
        if (DerogationStatus.PENDING.equals(derogation.getStatus())) {
            derogationRepository.delete(derogation);
        } else {
            throw new BadRequestException("Can only delete derogations in PENDING status");
        }
    }

    @Override
    public DerogationEntity findLatestByProjectId(Long projectId) {
        return derogationRepository.findTopByProjectIdentityIdOrderByCreatedAtDesc(projectId)
            .orElse(null);
    }


}