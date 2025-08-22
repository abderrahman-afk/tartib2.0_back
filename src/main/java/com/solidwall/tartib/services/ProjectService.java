package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.CreateDto;
import com.solidwall.tartib.dto.project.UpdateDto;
import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.implementations.ProjectImplementation;
import com.solidwall.tartib.repositories.ProjectRepository;

@Service
public class ProjectService implements ProjectImplementation {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public ProjectEntity getOne(Long id) {
        Optional<ProjectEntity> data = projectRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("project not exist");
        }
    }

    @Override
    public ProjectEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<ProjectEntity> findAll() {
        if (!projectRepository.findAll().isEmpty()) {
            return projectRepository.findAll();
        } else {
            throw new NotFoundException("not exist any projects ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<ProjectEntity> data = projectRepository.findById(id);
        if (data.isPresent()) {
            projectRepository.deleteById(id);
        } else {
            throw new NotFoundException("project not exist");
        }
    }

    @Override
    public ProjectEntity create(CreateDto data) {

        Optional<ProjectEntity> project = projectRepository.findBySimepCode(data.getSimepCode());

        if (!project.isPresent()) {
            ProjectEntity newProject = new ProjectEntity();
            newProject.setSimepCode(data.getSimepCode());
            newProject.setSimepDate(data.getSimepDate());
            newProject.setObservation(data.getObservation());
            newProject.setActive(data.isActive());
            newProject.setStatus(data.isStatus());
            return projectRepository.save(newProject);
        } else {
            throw new FoundException("project exist");
        }

    }

    @Override
    public ProjectEntity update(Long id, UpdateDto data) {

        Optional<ProjectEntity> project = projectRepository.findById(id);

        if (project.isPresent()) {
            ProjectEntity updateProject = project.get();
            updateProject.setSimepCode(data.getSimepCode());
            updateProject.setSimepDate(data.getSimepDate());
            updateProject.setObservation(data.getObservation());
            updateProject.setActive(data.isActive());
            updateProject.setStatus(data.isStatus());
            return projectRepository.save(updateProject);
        } else {
            throw new NotFoundException("project not found");
        }
        
    }
}
