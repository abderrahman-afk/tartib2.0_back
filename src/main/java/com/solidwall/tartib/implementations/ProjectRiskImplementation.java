package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.risk.CreateDto;
import com.solidwall.tartib.dto.project.risk.UpdateDto;
import com.solidwall.tartib.entities.ProjectRiskEntity;

public interface ProjectRiskImplementation {

    ProjectRiskEntity getOne(Long id);

    ProjectRiskEntity findOne(Map<String, String> data);

    List<ProjectRiskEntity> findAll();

    void delete(Long id);

    ProjectRiskEntity create(CreateDto data);

    ProjectRiskEntity update(Long id, UpdateDto data);

     List<ProjectRiskEntity> findAllByProjectIdentity(long project);

}
