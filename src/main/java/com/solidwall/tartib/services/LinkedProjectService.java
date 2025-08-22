package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.linkedProject.CreateDto;
import com.solidwall.tartib.dto.linkedProject.UpdateDto;
import com.solidwall.tartib.entities.LinkedProjectEntity;
import com.solidwall.tartib.entities.ProjectZoneEntity;
import com.solidwall.tartib.implementations.LinkedProjectImplementation;
import com.solidwall.tartib.repositories.LinkedProjectRepository;
import com.solidwall.tartib.repositories.ProjectZoneRepository;

@Service
public class LinkedProjectService implements LinkedProjectImplementation {

    @Autowired
    LinkedProjectRepository LinkedProjectRepository;

    @Autowired
    ProjectZoneRepository projectZoneRepository;

    @Override
    public List<LinkedProjectEntity> findAll() {
        if (!LinkedProjectRepository.findAll().isEmpty()) {
            return LinkedProjectRepository.findAll();
        } else {
            throw new NotFoundException("not exist any LinkedProject");
        }
    }

    @Override
    public LinkedProjectEntity getOne(Long id) {
        Optional<LinkedProjectEntity> LinkedProject = LinkedProjectRepository.findById(id);
        if (LinkedProject.isPresent()) {
            return LinkedProject.get();
        } else {
            throw new NotFoundException("LinkedProject not exist");
        }
    }

    @Override
    public LinkedProjectEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public LinkedProjectEntity create(CreateDto data) {
        Optional<ProjectZoneEntity> projectZone = projectZoneRepository.findById(data.getProjectZone_id());
        if (projectZone.isPresent()) {
            Optional<LinkedProjectEntity> LinkedProject = LinkedProjectRepository.findByProjectCode(data.getProjectCode());
            if (!LinkedProject.isPresent()) {
                LinkedProjectEntity newLinkedProject = new LinkedProjectEntity();
                newLinkedProject.setProjectZone(projectZone.get());
                newLinkedProject.setProjectCode(data.getProjectCode());
                newLinkedProject.setDescription(data.getDescription());
                return LinkedProjectRepository.save(newLinkedProject);
            } else {
                throw new FoundException("LinkedProject already exist");
            }

        } else {
            throw new FoundException("there is no project zone to link this LinkedProject");
        }

    }

    @Override
    public LinkedProjectEntity update(Long id, UpdateDto data) {

        Optional<LinkedProjectEntity> LinkedProject = LinkedProjectRepository.findById(id);

        if (LinkedProject.isPresent()) {
            LinkedProjectEntity updatedLinkedProject = LinkedProject.get();
            updatedLinkedProject.setProjectCode(data.getProjectCode());
            updatedLinkedProject.setDescription(data.getDescription());
            return LinkedProjectRepository.save(updatedLinkedProject);
        } else {
            throw new NotFoundException("LinkedProject not found");
        }

    }

    @Override
    public void delete(Long id) {
        Optional<LinkedProjectEntity> LinkedProject = LinkedProjectRepository.findById(id);
        if (LinkedProject.isPresent()) {
            LinkedProjectRepository.deleteById(id);
        } else {
            throw new NotFoundException("LinkedProject not exist");
        }
    }
}
