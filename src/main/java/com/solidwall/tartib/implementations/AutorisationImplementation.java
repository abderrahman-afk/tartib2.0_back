package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.autorisation.*;
import com.solidwall.tartib.entities.AutorisationEntity;

public interface AutorisationImplementation {

    List<AutorisationEntity> findAll();

    AutorisationEntity getOne(Long id);

    AutorisationEntity findOne(Map<String, String> data);

    AutorisationEntity create(CreateDto data);

    AutorisationEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
