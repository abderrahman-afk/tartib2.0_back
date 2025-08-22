package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.entities.ComponentLogicEntity;
import com.solidwall.tartib.dto.componentlogic.*; 
public interface ComponentLogicImplementation {
    List<ComponentLogicEntity> findAll();

    ComponentLogicEntity findOne();

    ComponentLogicEntity getOne(Long id);

    ComponentLogicEntity create(CreateDto data);

    ComponentLogicEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
