package com.solidwall.tartib.services;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.zone.CreateDto;
import com.solidwall.tartib.dto.project.zone.UpdateDto;
import com.solidwall.tartib.entities.LinkedProjectEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectLogicEntity;
import com.solidwall.tartib.entities.ProjectZoneEntity;
import com.solidwall.tartib.entities.StakeHolderEntity;
import com.solidwall.tartib.implementations.ProjectZoneImplementation;
import com.solidwall.tartib.repositories.DeanshipRepository;
import com.solidwall.tartib.repositories.DelegationRepository;
import com.solidwall.tartib.repositories.DistrictRepository;
import com.solidwall.tartib.repositories.GovernorateRepository;
import com.solidwall.tartib.repositories.LinkedProjectRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectLogicRepository;
import com.solidwall.tartib.repositories.ProjectZoneRepository;
import com.solidwall.tartib.repositories.StakeholderRepository;

@Service
public class ProjectZoneService implements ProjectZoneImplementation {

  @Autowired
  ProjectZoneRepository projectZoneRepository;

  @Autowired
  ProjectIdentityRepository projectIdentityRepository;

  @Autowired
  DeanshipRepository deanshipRepository;

  @Autowired
  DelegationRepository delegationRepository;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  GovernorateRepository governorateRepository;
  @Autowired
  LinkedProjectRepository linkedProjectRepository;
  @Autowired
  StakeholderRepository stakeholderRepository;

  @Override
  public ProjectZoneEntity getOne(Long id) {
    Optional<ProjectZoneEntity> projectZone = projectZoneRepository.findById(id);
    if (projectZone.isPresent()) {
      return projectZone.get();
    } else {
      throw new NotFoundException("project zone not exist");
    }
  }

  @Override
  public ProjectZoneEntity findOne(Map<String, String> data) {

    if (data.get("projectIdentity") != null) {
      Long projectId = Long.parseLong(data.get("projectIdentity"));
      Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(projectId);
      if (!projectIdentity.isPresent())
        // throw new NotFoundException("projectIdentity not found");
         return null;
      Optional<ProjectZoneEntity> projectZone = projectZoneRepository.findByProjectIdentity(projectIdentity.get());
       return projectZone.orElse(null);

      // if (!projectZone.isPresent())
      //   // throw new NotFoundException("projectIdentity zone not found");
      //    return null;
    }
    throw new BadRequestException("param not exist");

  }

  @Override
  public List<ProjectZoneEntity> findAll() {
    if (!projectZoneRepository.findAll().isEmpty()) {
      return projectZoneRepository.findAll();
    } else {
      throw new NotFoundException("not exist any project zone ");
    }
  }

  @Override
  public ProjectZoneEntity create(CreateDto data) {

      Optional<ProjectIdentityEntity> projectidentity = projectIdentityRepository.findById(data.getProjectIdentity());
   
    if(projectidentity.isPresent()){
      
      Optional<ProjectZoneEntity> checkprojectZone = projectZoneRepository.findByProjectIdentity(projectidentity.get());
      if (checkprojectZone.isPresent()) {
        projectZoneRepository.delete(checkprojectZone.get());
      }
      ProjectZoneEntity newProjectZone = new ProjectZoneEntity();
     
      newProjectZone.setProjectIdentity(projectidentity.get());
      newProjectZone.setFunctionStatus(data.getFunctionStatus());
      newProjectZone.setObservation(data.getObservation());
      newProjectZone.setJustification(data.getJustification());

      newProjectZone.setDistricts(districtRepository.findAllById(data.getDistricts()));
      newProjectZone.setGovernorates(governorateRepository.findAllById(data.getGovernorates()));
      newProjectZone.setDelegations(delegationRepository.findAllById(data.getDelegations()));
      newProjectZone.setDeanships(deanshipRepository.findAllById(data.getDeanships()));

    
 if (data.getStakeholders() != null) {
             List<StakeHolderEntity> stakeholders = data.getStakeholders().stream()
                .map(stakeholderDto -> {
                    StakeHolderEntity stakeholder = new StakeHolderEntity();
                    stakeholder.setName(stakeholderDto.getName());
                    stakeholder.setDescription(stakeholderDto.getDescription());
                    stakeholder.setType(stakeholderDto.getType());
                    stakeholder.setProjectZone(newProjectZone);
                    return stakeholder;
                })
                .collect(Collectors.toList());
            newProjectZone.setStakeholders(stakeholders);
        }


  
        
 if (data.getLinkedProjects() != null) {
            List<LinkedProjectEntity> linkedProjects = data.getLinkedProjects().stream()
                .map(linkedProjectDto -> {
                    LinkedProjectEntity linkedProject = new LinkedProjectEntity();
                    linkedProject.setProjectCode(linkedProjectDto.getProjectCode());
                    linkedProject.setDescription(linkedProjectDto.getDescription());
                    linkedProject.setProjectZone(newProjectZone);
                    return linkedProject;
                })
                .collect(Collectors.toList());
            newProjectZone.setLinkedProjects(linkedProjects);
        }

        
      return projectZoneRepository.save(newProjectZone);
    }
      else {
      throw new NotFoundException("not exist any project identy to link this zone ");
    }

  }

  @Override
  public ProjectZoneEntity update(Long id, UpdateDto data) {

    Optional<ProjectZoneEntity> projectZone = projectZoneRepository.findById(id);
    if (projectZone.isPresent()) {
      ProjectZoneEntity updateProjectZone = projectZone.get();
      updateProjectZone.setFunctionStatus(data.getFunctionStatus());
      updateProjectZone.setObservation(data.getObservation());
      updateProjectZone.setJustification(data.getJustification());

      updateProjectZone.setStakeholders(null);
      updateProjectZone.setLinkedProjects(null);
      updateProjectZone.setDistricts(districtRepository.findAllById(data.getDistricts()));
      updateProjectZone.setGovernorates(governorateRepository.findAllById(data.getGovernorates()));
      updateProjectZone.setDelegations(delegationRepository.findAllById(data.getDelegations()));
      updateProjectZone.setDeanships(deanshipRepository.findAllById(data.getDeanships()));



      if (data.getStakeholders() != null) {
         stakeholderRepository.deleteByProjectZoneId(updateProjectZone.getId());
         List<StakeHolderEntity> stakeholders = data.getStakeholders().stream()
            .map(stakeholderDto -> {
                StakeHolderEntity stakeholder = new StakeHolderEntity();
                stakeholder.setName(stakeholderDto.getName());
                stakeholder.setDescription(stakeholderDto.getDescription());
                stakeholder.setType(stakeholderDto.getType());
                stakeholder.setProjectZone(updateProjectZone);
                return stakeholder;
            })
            .collect(Collectors.toList());
            updateProjectZone.setStakeholders(stakeholders);
    }



    
if (data.getLinkedProjects() != null) {
  linkedProjectRepository.deleteByProjectZoneId(updateProjectZone.getId());

         List<LinkedProjectEntity> linkedProjects = data.getLinkedProjects().stream()
            .map(linkedProjectDto -> {
                LinkedProjectEntity linkedProject = new LinkedProjectEntity();
                linkedProject.setProjectCode(linkedProjectDto.getProjectCode());
                linkedProject.setDescription(linkedProjectDto.getDescription());
                linkedProject.setProjectZone(updateProjectZone);
                return linkedProject;
            })
            .collect(Collectors.toList());
            updateProjectZone.setLinkedProjects(linkedProjects);
    }
    Optional<ProjectZoneEntity> checkprojectZone = projectZoneRepository.findByProjectIdentity(projectZone.get().getProjectIdentity());
      if (checkprojectZone.isPresent()) {
        projectZoneRepository.delete(checkprojectZone.get());
      }
 
      return projectZoneRepository.save(updateProjectZone);
    } else {
      throw new NotFoundException("project zone not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<ProjectZoneEntity> projectZone = projectZoneRepository.findById(id);
    if (projectZone.isPresent()) {
      projectZoneRepository.deleteById(id);
    } else {
      throw new NotFoundException("project zone not exist");
    }
  }

}
