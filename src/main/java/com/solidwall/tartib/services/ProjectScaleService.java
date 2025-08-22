package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.projectscale.*;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectScaleEntity;
import com.solidwall.tartib.entities.StudyEntity;
import com.solidwall.tartib.implementations.ProjectScaleImplementation;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectScaleRepository;
import com.solidwall.tartib.repositories.StudyRepository;

@Service
public class ProjectScaleService implements ProjectScaleImplementation {
   @Autowired
    private ProjectScaleRepository projectScaleRepository;
    @Autowired
    private ProjectIdentityRepository projectIdentityRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Override
    public List<ProjectScaleEntity> findAll() {
        List<ProjectScaleEntity> projectScales = projectScaleRepository.findAll();
        if (!projectScales.isEmpty()) {
            return projectScales;
        } else {
            throw new NotFoundException("No project scales found");
        }
    }

    @Override
    public ProjectScaleEntity getOne(Long id) {
        return projectScaleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Project scale not found"));
    }

    @Override
    public ProjectScaleEntity create(CreateDto data) {
        if(data.getMaximumBudget()<data.getMinimumBudget()){
            throw new FoundException("minimum budget is smaller that maximum");
        }
       List<ProjectScaleEntity> projectScaleEntitycheck = projectScaleRepository.findByMaximumBudgetAndMinimumBudget(data.getMinimumBudget(), data.getMaximumBudget());
    
       if(!projectScaleEntitycheck.isEmpty()){
            throw new FoundException("cet intervale intersect avec un autre intervalle du budget");
        }
  
        

        
        ProjectScaleEntity projectScale = new ProjectScaleEntity();
        projectScale.setName(data.getName());
        projectScale.setDescription(data.getDescription());
        projectScale.setMaximumBudget(data.getMaximumBudget());
        projectScale.setMinimumBudget(data.getMinimumBudget());
        projectScale.setActive(data.isActive());

        if (data.getStudies() != null && !data.getStudies().isEmpty()) {
            List<StudyEntity> studies = studyRepository.findAllById(data.getStudies());
            projectScale.setStudies(new ArrayList<>(studies));
        }

        return projectScaleRepository.save(projectScale);
    }

    @Override
    public ProjectScaleEntity update(Long id, UpdateDto data) {
        ProjectScaleEntity projectScale = projectScaleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Project scale not found"));

        projectScale.setName(data.getName());
        projectScale.setName(data.getName());
        projectScale.setDescription(data.getDescription());
        projectScale.setMaximumBudget(data.getMaximumBudget());
        projectScale.setMinimumBudget(data.getMinimumBudget());
        projectScale.setActive(data.isActive());

        if (data.getStudies() != null) {
            List<StudyEntity> studies = studyRepository.findAllById(data.getStudies());
            projectScale.setStudies(new ArrayList<>(studies));
        }
        if(data.getMaximumBudget()<data.getMinimumBudget()){
            throw new FoundException("minimum budget is smaller that maximum");
        }
       List<ProjectScaleEntity> projectScaleEntitycheck = this.findByMaximumBudgetAndMinimumBudget(data.getMinimumBudget(), data.getMaximumBudget());
    
       if(!projectScaleEntitycheck.contains(projectScale) && !projectScaleEntitycheck.isEmpty()){
            throw new FoundException("cet intervale intersect avec un autre intervalle du budget");
       }
   
        return projectScaleRepository.save(projectScale);
    }

    @Override
    public void delete(Long id) {
        ProjectScaleEntity projectScale = projectScaleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Project scale not found"));
        projectScaleRepository.delete(projectScale);
    }

    @Override
    public ProjectScaleEntity addStudyToProjectScale(Long projectId, Long studyId) {
        ProjectScaleEntity projectScale = projectScaleRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project scale not found"));
        StudyEntity study = studyRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException("Study not found"));

        projectScale.getStudies().add(study);
       return projectScaleRepository.save(projectScale);
    }

    @Override
    public void addStudiesToProjectScale(Long projectId, List<Long> studyIds) {
        ProjectScaleEntity projectScale = projectScaleRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project scale not found"));
        List<StudyEntity> studies = studyRepository.findAllById(studyIds);
        projectScale.setStudies(new ArrayList<>());
        projectScale.getStudies().addAll(studies);
        projectScaleRepository.save(projectScale);
    }

    @Override
    public List<ProjectScaleEntity> findByMaximumBudgetAndMinimumBudget(Long minimumBudget,Long maxmimumBudget) {
        return projectScaleRepository.findByMaximumBudgetAndMinimumBudget(minimumBudget,maxmimumBudget);
    }

    @Override
    public List<ProjectScaleEntity> findByProjectIdentity(Long id) {

        Optional<ProjectIdentityEntity> projectIdentityEntity = projectIdentityRepository.findById(id);
        if(projectIdentityEntity.isPresent()){
            List<ProjectScaleEntity> projectScales = projectScaleRepository.findByProjectIdentity(projectIdentityEntity.get());
            if (!projectScales.isEmpty()) {
                return projectScales;
            } else {
                throw new NotFoundException("No project scales found");
            }
        }
        else {
            throw new NotFoundException("No project identity found");
        }
    }
}
