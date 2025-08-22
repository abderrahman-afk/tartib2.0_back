package com.solidwall.tartib.implementations;

import com.solidwall.tartib.dto.ponderation.CreateDto;
import com.solidwall.tartib.dto.ponderation.UpdateDto;
import com.solidwall.tartib.entities.PonderationEntity;
import java.util.List;

public interface PonderationImplementation {
    List<PonderationEntity> findAll();
    PonderationEntity getOne(Long id);
    PonderationEntity getByEvaluationGridId(Long gridId);  // Add this method
    PonderationEntity create(CreateDto data);
    PonderationEntity update(Long id, UpdateDto data);
    void delete(Long id);
    PonderationEntity activatePonderation(Long gridId,boolean status);
}