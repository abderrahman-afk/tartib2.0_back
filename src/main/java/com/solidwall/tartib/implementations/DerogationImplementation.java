package com.solidwall.tartib.implementations;


import com.solidwall.tartib.dto.derrogation.*;
import com.solidwall.tartib.entities.DerogationEntity;

import java.util.List;
import java.util.Map;

public interface DerogationImplementation {
    /**
     * Find all derogations
     */
    List<DerogationEntity> findAll();

    /**
     * Find derogations by status
     */
    List<DerogationEntity> findByStatus(com.solidwall.tartib.enums.DerogationStatus status);

    /**
     * Find one derogation by parameters (project and grid)
     */
    DerogationEntity findOne(Map<String, String> data);

    /**
     * Get one derogation by id
     */
    DerogationEntity getOne(Long id);

    /**
     * Create a new derogation
     */
    DerogationEntity create(CreateDto data);
    public DerogationEntity findLatestByProjectId(Long projectId) ;
    /**
     * Update an existing derogation
     */
    DerogationEntity update(Long id, UpdateDto data);

    /**
     * Delete a derogation
     */
    void delete(Long id);

    /**
     * Accept a derogation
     */
    DerogationEntity acceptDerogation(Long id, String decidedBy);

    /**
     * Reject a derogation
     */
    DerogationEntity rejectDerogation(Long id, String decidedBy);

 
}