package com.solidwall.tartib.services;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.logic.CreateDto;
import com.solidwall.tartib.dto.project.logic.UpdateDto;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectLogicEntity;
import com.solidwall.tartib.entities.ComponentLogicEntity;
import com.solidwall.tartib.implementations.ProjectLogicImplementation;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectLogicRepository;
import com.solidwall.tartib.repositories.ComponentLogicRepository;

@Service
public class ProjectLogicService implements ProjectLogicImplementation {

  @Autowired
  ProjectLogicRepository ProjectLogicRepository;

  @Autowired
  ProjectIdentityRepository projectIdentityRepository;

  @Autowired
  ComponentLogicRepository ComponentLogicRepository;

  @Override
  public ProjectLogicEntity getOne(Long id) {
    Optional<ProjectLogicEntity> ProjectLogic = ProjectLogicRepository.findById(id);
    if (ProjectLogic.isPresent()) {
      return ProjectLogic.get();
    } else {
      throw new NotFoundException("project logic not exist");
    }
  }

  @Override
  public ProjectLogicEntity findOne(Map<String, String> data) {

    if (data.get("projectIdentity") != null) {
      Long projectId = Long.parseLong(data.get("projectIdentity"));
      Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(projectId);
      if (!projectIdentity.isPresent())
        throw new NotFoundException("projectIdentity not found");
      Optional<ProjectLogicEntity> ProjectLogic = ProjectLogicRepository.findByProjectIdentity(projectIdentity.get());
      if (!ProjectLogic.isPresent())
        throw new NotFoundException("projectIdentity logic not found");
      return ProjectLogic.get();
    }
    throw new BadRequestException("param not exist");

  }

  @Override
  public List<ProjectLogicEntity> findAll() {
    if (!ProjectLogicRepository.findAll().isEmpty()) {
      return ProjectLogicRepository.findAll();
    } else {
      throw new NotFoundException("not exist any project logic ");
    }
  }

  @Override
  public ProjectLogicEntity create(CreateDto data) {

    Optional<ProjectIdentityEntity> projectidentity = projectIdentityRepository.findById(data.getProjectIdentity());

    if (projectidentity.isPresent()) {
      Optional<ProjectLogicEntity> ProjectLogic = ProjectLogicRepository.findByProjectIdentity(projectidentity.get());
      if (ProjectLogic.isPresent()) {
        ProjectLogicRepository.delete(ProjectLogic.get());
      }

      ProjectLogicEntity newProjectLogic = new ProjectLogicEntity();

      newProjectLogic.setProjectIdentity(projectidentity.get());
      newProjectLogic.setGeneralObjective(data.getGeneralObjective());
      newProjectLogic.setSpecific_objective(data.getSpecific_objective());
      newProjectLogic.setResults(data.getResults());
      newProjectLogic.setYearEnd(data.getYearEnd());
      newProjectLogic.setYearStart(data.getYearStart());
      newProjectLogic.setDocumentCadre(data.getDocumentCadre());
      newProjectLogic.setDocumentPlanTravail(data.getDocumentPlanTravail());

      if (data.getComponentLogics() != null) {
        List<ComponentLogicEntity> ComponentLogics = data.getComponentLogics().stream()
            .map(ComponentLogicDto -> {
              ComponentLogicEntity ComponentLogic = new ComponentLogicEntity();
              ComponentLogic.setName(ComponentLogicDto.getName());
              ComponentLogic.setDescription(ComponentLogicDto.getDescription());
              ComponentLogic.setCout(ComponentLogicDto.getCout());

              ComponentLogic.setProjectLogic(newProjectLogic);
              return ComponentLogic;
            })
            .collect(Collectors.toList());
        newProjectLogic.setComponentLogics(ComponentLogics);
      }

      return ProjectLogicRepository.save(newProjectLogic);
    } else {
      throw new NotFoundException("not exist any project identy to link this logic ");
    }

  }

  @Override
  public ProjectLogicEntity update(Long id, UpdateDto data) {

    Optional<ProjectLogicEntity> ProjectLogic = ProjectLogicRepository.findById(id);

    if (ProjectLogic.isPresent()) {
      ProjectLogicEntity updateProjectLogic = ProjectLogic.get();
      updateProjectLogic.setGeneralObjective(data.getGeneralObjective());
      updateProjectLogic.setSpecific_objective(data.getSpecific_objective());
      updateProjectLogic.setResults(data.getResults());
      updateProjectLogic.setYearEnd(data.getYearEnd());
      updateProjectLogic.setYearStart(data.getYearStart());
      updateProjectLogic.setDocumentCadre(data.getDocumentCadre());
      updateProjectLogic.setDocumentPlanTravail(data.getDocumentPlanTravail());

      if (data.getComponentLogics() != null) {
        ComponentLogicRepository.deleteByProjectLogicId(updateProjectLogic.getId());
        List<ComponentLogicEntity> ComponentLogics = data.getComponentLogics().stream()
            .map(ComponentLogicDto -> {
              ComponentLogicEntity ComponentLogic = new ComponentLogicEntity();
              ComponentLogic.setProjectLogic(updateProjectLogic);
              ComponentLogic.setName(ComponentLogicDto.getName());
              ComponentLogic.setDescription(ComponentLogicDto.getDescription());
              ComponentLogic.setCout(ComponentLogicDto.getCout());
              return ComponentLogic;
            })
            .collect(Collectors.toList());
        updateProjectLogic.setComponentLogics(ComponentLogics);
      }

      return ProjectLogicRepository.save(updateProjectLogic);
    } else {
      throw new NotFoundException("project logic not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<ProjectLogicEntity> ProjectLogic = ProjectLogicRepository.findById(id);
    if (ProjectLogic.isPresent()) {
      ProjectLogicRepository.deleteById(id);
    } else {
      throw new NotFoundException("project logic not exist");
    }
  }

  @Override
  public Long getSommeComposant(Long id) {
    Optional<ProjectLogicEntity> projectLogicOptional = ProjectLogicRepository.findById(id);
    long somme = 0;
    ProjectLogicEntity projectLogic = projectLogicOptional
        .orElseThrow(() -> new NotFoundException("Project logic not found"));

    // Get component logics and calculate sum
    if (projectLogic.getComponentLogics() != null) {
      somme = projectLogic.getComponentLogics().stream()
          .mapToLong(ComponentLogicEntity::getCout) // Directly map to long for sum
          .sum(); // Sum all values
    }

    return somme;
  }

}
