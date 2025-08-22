package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.identity.CreateDto;
import com.solidwall.tartib.dto.project.identity.UpdateDto;
import com.solidwall.tartib.entities.ProjectBlockEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.enums.ProjectStaut;

public interface ProjectIdentityImplementation {

  List<ProjectIdentityEntity> findAll(Boolean archived);

  ProjectIdentityEntity findOne(Map<String, String> data);

  ProjectIdentityEntity getOne(Long id);

  ProjectIdentityEntity create(CreateDto data);

  ProjectIdentityEntity update(Long id, UpdateDto data);

  void delete(Long id);

  void archive(Long id);

  ProjectIdentityEntity validateProject(Long id);

  ProjectIdentityEntity finalvalidateProject(Long id);

  void blockProject(Long projectId, String reason);

  ProjectIdentityEntity unBlockProject(Long id);

  ProjectIdentityEntity ForcevalidateProject(Long id);

  ProjectBlockEntity getBlock(Long id);

  ProjectBlockEntity updateBlockreason(Long id, String reason);

  Long countProjectsByStatus(ProjectStaut status);

  Long getTotalProjectsBudget();

  public void unarchive(Long id);

}
