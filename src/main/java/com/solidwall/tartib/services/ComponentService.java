package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.component.CreateDto;
import com.solidwall.tartib.dto.component.UpdateDto;
import com.solidwall.tartib.entities.ComponentEntity;
import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.implementations.ComponentImplementation;
import com.solidwall.tartib.repositories.ComponentRepository;
import com.solidwall.tartib.repositories.ProjectPlanRepository;
import com.solidwall.tartib.repositories.ProjectRepository;

@Service
public class ComponentService implements ComponentImplementation {

  @Autowired
  ComponentRepository componentRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  ProjectPlanRepository projectPlanRepository;

  @Override
  public ComponentEntity getOne(Long id) {
    Optional<ComponentEntity> component = componentRepository.findById(id);
    if (component.isPresent()) {
      return component.get();
    } else {
      throw new NotFoundException("component not exist");
    }
  }

  @Override
  public ComponentEntity findOne(Map<String, String> data) {
    throw new BadRequestException("param not exist");

  }

  @Override
  public List<ComponentEntity> findAll() {
    if (!componentRepository.findAll().isEmpty()) {
      return componentRepository.findAll();
    } else {
      throw new NotFoundException("not exist any component");
    }
  }

  @Override
  public ComponentEntity create(CreateDto data) {
    Optional<ProjectEntity> project = projectRepository.findById(data.getProject());
    Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findById(data.getProjectPlan());
    ComponentEntity component = data.getComponent() != null
        ? componentRepository.findById(data.getComponent()).orElse(null)
        : null;
    ComponentEntity newComponent = new ComponentEntity();
    newComponent.setName(data.getName());
    newComponent.setCode(data.getCode());
    newComponent.setDescription(data.getDescription());
    newComponent.setSubComponent(data.isSubComponent());
    newComponent.setComponent(component);
    newComponent.setProject(project.get());
    newComponent.setProjectPlan(projectPlan.get());
    return componentRepository.save(newComponent);
  }

  @Override
  public ComponentEntity update(Long id, UpdateDto data) {
    Optional<ComponentEntity> componentExist = componentRepository.findById(id);
    Optional<ProjectEntity> project = projectRepository.findById(data.getProject());
    Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findById(data.getProjectPlan());
    ComponentEntity component = data.getComponent() != null
        ? componentRepository.findById(data.getComponent()).orElse(null)
        : null;
    if (componentExist.isPresent()) {
      ComponentEntity updateComponent = componentExist.get();
      updateComponent.setCode(data.getCode());
      updateComponent.setName(data.getName());
      updateComponent.setDescription(data.getDescription());
      updateComponent.setSubComponent(data.isSubComponent());
      updateComponent.setComponent(component);
      updateComponent.setProject(project.get());
      updateComponent.setProjectPlan(projectPlan.get());
      return componentRepository.save(updateComponent);
    } else {
      throw new NotFoundException("component not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<ComponentEntity> component = componentRepository.findById(id);
    if (component.isPresent()) {
      componentRepository.deleteById(id);
    } else {
      throw new NotFoundException("component not exist");
    }
  }

}
