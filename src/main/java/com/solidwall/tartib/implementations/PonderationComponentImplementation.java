package com.solidwall.tartib.implementations;
import com.solidwall.tartib.dto.ponderationComponentDto.UpdateDto;
import com.solidwall.tartib.dto.ponderationComponentDto.CreateDto;


import java.util.List;

import com.solidwall.tartib.entities.PonderationComponentEntity;

public interface PonderationComponentImplementation {
 List<PonderationComponentEntity> findAll();
    PonderationComponentEntity getOne(Long id);
    PonderationComponentEntity create(Long ponderationId, CreateDto data);
    PonderationComponentEntity update(Long id, UpdateDto data);
    void delete(Long id);
}
