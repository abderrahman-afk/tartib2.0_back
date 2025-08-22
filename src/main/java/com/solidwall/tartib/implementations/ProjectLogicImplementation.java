package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.logic.*;
import com.solidwall.tartib.entities.ProjectLogicEntity;

public interface ProjectLogicImplementation {
    List<ProjectLogicEntity> findAll();

    ProjectLogicEntity findOne(Map<String, String> data);

    ProjectLogicEntity getOne(Long id);

    ProjectLogicEntity create(CreateDto data);

    ProjectLogicEntity update(Long id, UpdateDto data);

    void delete(Long id);
    
    Long getSommeComposant(Long id);
}
