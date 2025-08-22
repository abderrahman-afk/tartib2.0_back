package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.validation.CreateDto;
import com.solidwall.tartib.dto.project.validation.UpdateDto;
import com.solidwall.tartib.entities.ProjectValidationEntity;

public interface ProjectValidationImplementation {

    ProjectValidationEntity getOne(Long id);

    ProjectValidationEntity findOne(Map<String, String> data);

    List<ProjectValidationEntity> findAll();

    void delete(Long id);

    ProjectValidationEntity create(CreateDto data);

    ProjectValidationEntity update(Long id, UpdateDto data);
}
