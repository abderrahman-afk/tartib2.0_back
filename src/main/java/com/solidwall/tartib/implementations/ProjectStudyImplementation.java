package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.study.CreateDto;
import com.solidwall.tartib.dto.project.study.UpdateDto;
import com.solidwall.tartib.entities.ProjectStudyEntity;

public interface ProjectStudyImplementation {

    List<ProjectStudyEntity> findAll();

    ProjectStudyEntity findOne(Map<String, String> data);

    ProjectStudyEntity getOne(Long id);

    ProjectStudyEntity create(CreateDto data);

    ProjectStudyEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
