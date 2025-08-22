package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.entities.AdmissibilityCriteriaEntity;

public interface AdmissibilityCriteriaImplementation {

    List<AdmissibilityCriteriaEntity> findAll();

    AdmissibilityCriteriaEntity findOne();

    AdmissibilityCriteriaEntity getOne(Long id);

    AdmissibilityCriteriaEntity create(AdmissibilityCriteriaEntity data);

    AdmissibilityCriteriaEntity update(Long id, AdmissibilityCriteriaEntity data);

    void delete(Long id);
}
