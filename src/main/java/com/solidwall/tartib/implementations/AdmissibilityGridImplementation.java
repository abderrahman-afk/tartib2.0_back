package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.entities.AdmissibilityGridEntity;
import com.solidwall.tartib.dto.admissibilitygrid.*;


public interface AdmissibilityGridImplementation {

    List<AdmissibilityGridEntity> findAll();

    AdmissibilityGridEntity findOne(Map<String, String> reqParam);

    AdmissibilityGridEntity getOne(Long id);

    AdmissibilityGridEntity create(CreateDto data);

    AdmissibilityGridEntity update(Long id, UpdateDto data);

    void delete(Long id);
    
    AdmissibilityGridEntity activateGrid(Long id,Boolean status);
   List<AdmissibilityGridEntity> desactivateAll();

}
