package com.solidwall.tartib.services;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.study.CreateDto;
import com.solidwall.tartib.dto.project.study.UpdateDto;
import com.solidwall.tartib.entities.AutorisationEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectScaleEntity;
import com.solidwall.tartib.entities.ProjectStudyEntity;
import com.solidwall.tartib.entities.StudyForProject;
import com.solidwall.tartib.implementations.ProjectStudyImplementation;
import com.solidwall.tartib.repositories.AutorisationRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectLogicRepository;
import com.solidwall.tartib.repositories.ProjectScaleRepository;
import com.solidwall.tartib.repositories.ProjectStudyRepository;
import com.solidwall.tartib.repositories.StudyForProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectStudyService implements ProjectStudyImplementation {

  @Autowired
  ProjectStudyRepository projectStudyRepository;

  @Autowired
  ProjectIdentityRepository projectIdentityRepository;


  @Autowired
  AutorisationRepository autorisationRepository;

  @Autowired
  ProjectScaleRepository projectScaleRepository;

  @Autowired
  StudyForProjectRepository studyForProjectRepository;

  @Override
  public List<ProjectStudyEntity> findAll() {
    if (!projectStudyRepository.findAll().isEmpty()) {
      return projectStudyRepository.findAll();
    } else {
      throw new NotFoundException("not exist any project study");
    }
  }

  @Override
  public ProjectStudyEntity getOne(Long id) {
    Optional<ProjectStudyEntity> projectStudy = projectStudyRepository.findById(id);
    if (projectStudy.isPresent()) {
      return projectStudy.get();
    } else {
      throw new NotFoundException("project study not exist");
    }
  }
  @Override
    @Transactional
    public ProjectStudyEntity findOne(Map<String, String> data) {
        if (data.get("projectIdentity") != null) {
            Long projectId = Long.parseLong(data.get("projectIdentity"));
            Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(projectId);
            
            if (!project.isPresent()) {
                throw new NotFoundException("project not found");
            }

            // Get the ProjectStudy if it exists
            Optional<ProjectStudyEntity> projectStudy = projectStudyRepository.findByProjectIdentity(project.get());
            
            // If ProjectStudy exists, ensure studies from ProjectScale are properly linked
            if (projectStudy.isPresent()) {
                ProjectStudyEntity existingProjectStudy = projectStudy.get();
           
                
                // Ensure all scale studies are included in the ProjectStudy
            
                
                 return existingProjectStudy;
            }
            
            // If no ProjectStudy exists, return null (frontend will handle initialization)
            return null;
        }
        throw new BadRequestException("param not exist");
    }

    @Override
    @Transactional
    public ProjectStudyEntity create(CreateDto data) {
        Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(data.getProjectIdentity());
        
        if (!project.isPresent()) {
            throw new NotFoundException("project not found");
        }
     

        ProjectStudyEntity newProjectStudy = new ProjectStudyEntity();
        newProjectStudy.setProjectIdentity(project.get());

        // Handle autorisations
        if (data.getAutorisations() != null) {
            List<AutorisationEntity> autorisations = data.getAutorisations().stream()
                .map(autorisationDto -> {
                    AutorisationEntity autorisation = new AutorisationEntity();
                    autorisation.setProjectStudy(newProjectStudy);
                    autorisation.setName(autorisationDto.getName());
                    autorisation.setObservation(autorisationDto.getObservation());
                    autorisation.setValidateur(autorisationDto.getValidateur());
                    return autorisation;
                })
                .collect(Collectors.toList());
            newProjectStudy.setAutorisations(autorisations);
        }

        // Get studies from ProjectScale
  

        // Combine scale studies with new studies from the request
         if (data.getStudies() != null) {
            List<StudyForProject> newStudies = data.getStudies().stream()
                .map(studyDto -> {
                    StudyForProject study = new StudyForProject();
                    study.setName(studyDto.getName());
                    study.setDescription(studyDto.getDescription());
                    study.setRealisationDate(studyDto.getRealisationDate());
                    study.setState(studyDto.getState());
                    study.setProjectStudy(newProjectStudy);
                    return study;
                })
                .collect(Collectors.toList());
        newProjectStudy.setStudies(newStudies);

         }
        
        return projectStudyRepository.save(newProjectStudy);
    }

    @Override
    @Transactional
    public ProjectStudyEntity update(Long id, UpdateDto data) {
        Optional<ProjectStudyEntity> projectStudy = projectStudyRepository.findById(id);
        Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(data.getProjectIdentity());

        if (!projectStudy.isPresent()) {
            throw new NotFoundException("project study not found");
        }

        ProjectStudyEntity updateProjectStudy = projectStudy.get();
        updateProjectStudy.setProjectIdentity(project.get());

        // Update autorisations
        if (data.getAutorisations() != null) {
          autorisationRepository.deleteByProjectStudyId(updateProjectStudy.getId());

            List<AutorisationEntity> autorisations = data.getAutorisations().stream()
                .map(autorisationDto -> {
                  
                    AutorisationEntity autorisation = new AutorisationEntity();
                    autorisation.setName(autorisationDto.getName());
                    autorisation.setObservation(autorisationDto.getObservation());
                    autorisation.setValidateur(autorisationDto.getValidateur());
                    autorisation.setProjectStudy(updateProjectStudy);

                    return autorisation;
                })
                .collect(Collectors.toList());
            updateProjectStudy.setAutorisations(autorisations);
        }


        // Combine scale studies with updated studies from the request
        if (data.getStudies() != null) {
          studyForProjectRepository.deleteByProjectStudyId(updateProjectStudy.getId());

            List<StudyForProject> updatedStudies = data.getStudies().stream()
                .map(studyDto -> {
                    StudyForProject study = new StudyForProject();
                    study.setName(studyDto.getName());
                    study.setDescription(studyDto.getDescription());
                    study.setRealisationDate(studyDto.getRealisationDate());
                    study.setState(studyDto.getState());
                    study.setProjectStudy(updateProjectStudy);
                    return study;
                })
                .collect(Collectors.toList());
                updateProjectStudy.setStudies(updatedStudies);
              }
              

        return projectStudyRepository.save(updateProjectStudy);
    }


  @Override
  public void delete(Long id) {
    Optional<ProjectStudyEntity> projectStudy = projectStudyRepository.findById(id);
    if (projectStudy.isPresent()) {
      projectStudyRepository.deleteById(id);
    } else {
      throw new NotFoundException("project study not exist");
    }
  }
}
