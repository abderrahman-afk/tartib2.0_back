package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.validation.CreateDto;
import com.solidwall.tartib.dto.project.validation.UpdateDto;
import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.entities.ProjectValidationEntity;
import com.solidwall.tartib.implementations.ProjectValidationImplementation;
import com.solidwall.tartib.repositories.ProjectRepository;
import com.solidwall.tartib.repositories.ProjectValidationRepository;

@Service
public class ProjectValidationService implements ProjectValidationImplementation {

  @Autowired
  ProjectValidationRepository projectValidationRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Override
  public List<ProjectValidationEntity> findAll() {
    if (!projectValidationRepository.findAll().isEmpty()) {
      return projectValidationRepository.findAll();
    } else {
      throw new NotFoundException("not exist any project validation");
    }
  }

  @Override
  public ProjectValidationEntity getOne(Long id) {
    Optional<ProjectValidationEntity> validation = projectValidationRepository.findById(id);
    if (validation.isPresent()) {
      return validation.get();
    } else {
      throw new NotFoundException("project validation not exist");
    }
  }

  @Override
  public ProjectValidationEntity findOne(Map<String, String> data) {
    if (data.get("project") != null) {
      Long projectId = Long.parseLong(data.get("project"));
      Optional<ProjectEntity> project = projectRepository.findById(projectId);
      if (!project.isPresent())
        throw new NotFoundException("project not found");
      Optional<ProjectValidationEntity> projectValidation = projectValidationRepository
          .findByProject(project.get());
      if (!projectValidation.isPresent())
        throw new NotFoundException("project validation not found");
      return projectValidation.get();
    }
    throw new BadRequestException("param not exist");
  }

  @Override
  public ProjectValidationEntity create(CreateDto data) {
    Optional<ProjectEntity> project = projectRepository.findById(data.getProject());
    if (project.isPresent()) {
      ProjectValidationEntity newProjectValidation = new ProjectValidationEntity();
      newProjectValidation.setProject(project.get());
      newProjectValidation.setAdditionalInformation(data.getAdditionalInformation());
      newProjectValidation.setAuthor(data.getAuthor());
      newProjectValidation.setDocumentName(data.getDocumentName());
      newProjectValidation.setSubmissionDate(data.getSubmissionDate());
      return projectValidationRepository.save(newProjectValidation);
    } else {
      throw new NotFoundException("project not found");
    }
  }

  @Override
  public ProjectValidationEntity update(Long id, UpdateDto data) {
    Optional<ProjectValidationEntity> projectValidation = projectValidationRepository.findById(id);
    Optional<ProjectEntity> project = projectRepository.findById(data.getProject());
    if (projectValidation.isPresent()) {
      ProjectValidationEntity updateProjectValidation = projectValidation.get();
      updateProjectValidation.setProject(project.get());
      updateProjectValidation.setAdditionalInformation(data.getAdditionalInformation());
      updateProjectValidation.setAuthor(data.getAuthor());
      updateProjectValidation.setDocumentName(data.getDocumentName());
      updateProjectValidation.setSubmissionDate(data.getSubmissionDate());
      return projectValidationRepository.save(updateProjectValidation);
    } else {
      throw new NotFoundException("project validation not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<ProjectValidationEntity> projectValidation = projectValidationRepository.findById(id);
    if (projectValidation.isPresent()) {
      projectValidationRepository.deleteById(id);
    } else {
      throw new NotFoundException("project validation not exist");
    }
  }

}
