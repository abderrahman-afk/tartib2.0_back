package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.responseadmissibilitygrid.CreateDto;
import com.solidwall.tartib.dto.responseadmissibilitygrid.UpdateDto;
import com.solidwall.tartib.entities.AdmissibilityResponseGridEntity;

public interface AdmissibilityResponseImplementation {
    List<AdmissibilityResponseGridEntity> findAll();
    AdmissibilityResponseGridEntity findOne(Map<String, String> data);
    AdmissibilityResponseGridEntity getOne(Long id);
    AdmissibilityResponseGridEntity create(CreateDto data);
    AdmissibilityResponseGridEntity update(Long id, UpdateDto data);
    void delete(Long id);
    AdmissibilityResponseGridEntity verifyAdmissibility(Long projectId);
    AdmissibilityResponseGridEntity submitDerogation(Long id, String derogationText);
    public AdmissibilityResponseGridEntity reevaluate(Long projectId);
    public Integer getLatestGridVersion();
}