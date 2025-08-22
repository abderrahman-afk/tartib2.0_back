package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.component.CreateDto;
import com.solidwall.tartib.dto.component.UpdateDto;
import com.solidwall.tartib.entities.ComponentEntity;

public interface ComponentImplementation {

    List<ComponentEntity> findAll();

    ComponentEntity getOne(Long id);

    ComponentEntity findOne(Map<String, String> data);

    ComponentEntity create(CreateDto data);

    ComponentEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
