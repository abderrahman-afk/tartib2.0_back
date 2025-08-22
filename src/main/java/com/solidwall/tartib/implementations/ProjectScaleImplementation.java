package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.projectscale.CreateDto;
import com.solidwall.tartib.dto.projectscale.UpdateDto;
import com.solidwall.tartib.entities.ProjectScaleEntity;

public interface ProjectScaleImplementation {
    List<ProjectScaleEntity> findAll();

    ProjectScaleEntity getOne(Long id);

    ProjectScaleEntity create(CreateDto data);

    ProjectScaleEntity update(Long id, UpdateDto data);

    void delete(Long id);

    ProjectScaleEntity addStudyToProjectScale(Long projectId, Long studyId);

    void addStudiesToProjectScale(Long projectId, List<Long> studyIds);

    public List<ProjectScaleEntity> findByMaximumBudgetAndMinimumBudget(Long minimumBudget,Long maxmimumBudget);
    public    List<ProjectScaleEntity> findByProjectIdentity(Long projectIdentity); 

}
