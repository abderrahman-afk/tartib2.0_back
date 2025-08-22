package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.strategy.CreateDto;
import com.solidwall.tartib.dto.project.strategy.UpdateDto;
import com.solidwall.tartib.entities.ProjectStrategyEntity;

public interface ProjectStrategyImplementation {

    List<ProjectStrategyEntity> findAll();

    ProjectStrategyEntity findOne(Map<String, String> data);

    ProjectStrategyEntity getOne(Long id);

    ProjectStrategyEntity create(CreateDto data);

    ProjectStrategyEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
