package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.PndAxisEntity;
import com.solidwall.tartib.entities.PndEntity;
import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectStrategyEntity;
import com.solidwall.tartib.entities.StrategyAxisEntity;
import com.solidwall.tartib.entities.StrategyEntity;
import com.solidwall.tartib.implementations.ProjectStrategyImplementation;
import com.solidwall.tartib.repositories.PndAxisRepository;
import com.solidwall.tartib.repositories.PndRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectRepository;
import com.solidwall.tartib.repositories.ProjectStrategyRepository;
import com.solidwall.tartib.repositories.StrategyAxisRepository;
import com.solidwall.tartib.repositories.StrategyRepository;
import com.solidwall.tartib.dto.project.strategy.CreateDto;
import com.solidwall.tartib.dto.project.strategy.UpdateDto;

@Service
public class ProjectStrategyService implements ProjectStrategyImplementation {

  @Autowired
  ProjectIdentityRepository projectIdentityRepository;

  @Autowired
  ProjectStrategyRepository projectStrategyRepository;

  @Autowired
  PndRepository pndRepository;

  @Autowired
  PndAxisRepository pndAxisRepository;

  @Autowired
  StrategyRepository strategyRepository;

  @Autowired
  StrategyAxisRepository strategyAxisRepository;

  @Override
  public List<ProjectStrategyEntity> findAll() {
    if (!projectStrategyRepository.findAll().isEmpty()) {
      return projectStrategyRepository.findAll();
    } else {
      throw new NotFoundException("not exist any project strategy");
    }
  }

  @Override
  public ProjectStrategyEntity getOne(Long id) {
    Optional<ProjectStrategyEntity> projectStrategy = projectStrategyRepository.findById(id);
    if (projectStrategy.isPresent()) {
      return projectStrategy.get();
    } else {
      throw new NotFoundException("project strategy not exist");
    }
  }

  @Override
  public ProjectStrategyEntity findOne(Map<String, String> data) {
    if (data.get("projectIdentity") != null) {
      Long projectId = Long.parseLong(data.get("projectIdentity"));
      Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(projectId);
      if (!projectIdentity.isPresent())
        throw new NotFoundException("projectIdentity not found");
      Optional<ProjectStrategyEntity> projectStrategy = projectStrategyRepository.findByProjectIdentity(projectIdentity.get());
      if (!projectStrategy.isPresent())
        throw new NotFoundException("projectIdentity identity not found");
      return projectStrategy.get();
    }
    throw new BadRequestException("param not exist");
  }

  @Override
  public ProjectStrategyEntity create(CreateDto data) {

    Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(data.getProjectIdentity());
    PndEntity pnd = data.getPnd() != null ? pndRepository.findById(data.getPnd()).orElse(null) : null;
    PndAxisEntity pndAxis = data.getPnd() != null ? pndAxisRepository.findById(data.getPndAxis()).orElse(null) : null;

    if(project.isPresent()) {
      ProjectStrategyEntity newProjectStrategy = new ProjectStrategyEntity();
      newProjectStrategy.setProjectIdentity(project.get());
      newProjectStrategy.setPnd(pnd);
      newProjectStrategy.setPndAxis(pndAxis);
      newProjectStrategy.setObjective(data.getObjective());
      newProjectStrategy.setDescription(data.getDescription());
      newProjectStrategy.setPndLocal(data.getPndLocal());
      newProjectStrategy.setPoliticalName(data.getPoliticalName());
      newProjectStrategy.setPoliticalAxe(data.getPoliticalAxe());
      newProjectStrategy.setReference(data.getReference());
      newProjectStrategy.setTerrority(data.getTerrority());
 
      return projectStrategyRepository.save(newProjectStrategy);
    } else {
      throw new NotFoundException("project not found");
    }

  }

  @Override
  public ProjectStrategyEntity update(Long id, UpdateDto data) {
    Optional<ProjectStrategyEntity> projectStrategy = projectStrategyRepository.findById(id);
    Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(data.getProjectIdentity());
    PndEntity pnd = data.getPnd() != null ? pndRepository.findById(data.getPnd()).orElse(null) : null;
    PndAxisEntity pndAxis = data.getPnd() != null ? pndAxisRepository.findById(data.getPndAxis()).orElse(null) : null;

    if (projectStrategy.isPresent()) {
      ProjectStrategyEntity updateProjectStrategy = projectStrategy.get();
      updateProjectStrategy.setProjectIdentity(project.get());
      updateProjectStrategy.setPnd(pnd);
      updateProjectStrategy.setPndAxis(pndAxis);
      updateProjectStrategy.setObjective(data.getObjective());
      updateProjectStrategy.setDescription(data.getDescription());
      updateProjectStrategy.setPndLocal(data.getPndLocal());
      updateProjectStrategy.setPoliticalName(data.getPoliticalName());
      updateProjectStrategy.setPoliticalAxe(data.getPoliticalAxe());
      updateProjectStrategy.setReference(data.getReference());
      updateProjectStrategy.setTerrority(data.getTerrority());

      return projectStrategyRepository.save(updateProjectStrategy);
    } else {
      throw new NotFoundException("project strategy not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<ProjectStrategyEntity> strategic = projectStrategyRepository.findById(id);
    if (strategic.isPresent()) {
      projectStrategyRepository.deleteById(id);
    } else {
      throw new NotFoundException("project strategy not exist");
    }
  }

}
