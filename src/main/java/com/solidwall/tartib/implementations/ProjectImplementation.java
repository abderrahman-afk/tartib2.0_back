package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.project.CreateDto;
import com.solidwall.tartib.dto.project.UpdateDto;
import com.solidwall.tartib.entities.ProjectEntity;

public interface ProjectImplementation {

    ProjectEntity getOne(Long id);

    ProjectEntity findOne();

    List<ProjectEntity> findAll();

    void delete(Long id);

    ProjectEntity create(CreateDto data);

    ProjectEntity update(Long id, UpdateDto data);
}
