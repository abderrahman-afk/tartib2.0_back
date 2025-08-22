package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.plan.CreateDto;
import com.solidwall.tartib.dto.project.plan.UpdateDto;
import com.solidwall.tartib.entities.ProjectPlanEntity;

public interface ProjectPlanImplementation {

    List<ProjectPlanEntity> findAll();

    ProjectPlanEntity findOne(Map<String, String> data);

    ProjectPlanEntity getOne(Long id);

    ProjectPlanEntity create(CreateDto data);

    ProjectPlanEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
