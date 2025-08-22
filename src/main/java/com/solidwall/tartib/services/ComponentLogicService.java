package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.componentlogic.CreateDto;
import com.solidwall.tartib.dto.componentlogic.UpdateDto;
import com.solidwall.tartib.entities.ComponentLogicEntity;
import com.solidwall.tartib.entities.ProjectLogicEntity;
import com.solidwall.tartib.implementations.ComponentLogicImplementation;
import com.solidwall.tartib.repositories.ComponentLogicRepository;
import com.solidwall.tartib.repositories.ProjectLogicRepository;

@Service
public class ComponentLogicService implements ComponentLogicImplementation {
        @Autowired
    ComponentLogicRepository ComponentLogicRepository;

    @Autowired
    ProjectLogicRepository projectLogicRepository;

    @Override
    public List<ComponentLogicEntity> findAll() {
        if (!ComponentLogicRepository.findAll().isEmpty()) {
            return ComponentLogicRepository.findAll();
        } else {
            throw new NotFoundException("not exist any ComponentLogic");
        }
    }

    @Override
    public ComponentLogicEntity getOne(Long id) {
        Optional<ComponentLogicEntity> ComponentLogic = ComponentLogicRepository.findById(id);
        if (ComponentLogic.isPresent()) {
            return ComponentLogic.get();
        } else {
            throw new NotFoundException("ComponentLogic not exist");
        }
    }

    @Override
    public ComponentLogicEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public ComponentLogicEntity create(CreateDto data) {
        Optional<ProjectLogicEntity> projectLogic = projectLogicRepository.findById(data.getProjectLogic_id());
        if (projectLogic.isPresent()) {
            Optional<ComponentLogicEntity> ComponentLogic = ComponentLogicRepository.findByName(data.getName());
            if (!ComponentLogic.isPresent()) {
                ComponentLogicEntity newComponentLogic = new ComponentLogicEntity();
                newComponentLogic.setProjectLogic(projectLogic.get());
                newComponentLogic.setName(data.getName());
                newComponentLogic.setDescription(data.getDescription());
                newComponentLogic.setCout(data.getCout());

                return ComponentLogicRepository.save(newComponentLogic);
            } else {
                throw new FoundException("ComponentLogic already exist");
            }

        } else {
            throw new FoundException("there is no project zone to link this ComponentLogic");
        }

    }

    @Override
    public ComponentLogicEntity update(Long id, UpdateDto data) {

        Optional<ComponentLogicEntity> ComponentLogic = ComponentLogicRepository.findById(id);

        if (ComponentLogic.isPresent()) {
            ComponentLogicEntity updatedComponentLogic = ComponentLogic.get();
            updatedComponentLogic.setName(data.getName());
            updatedComponentLogic.setDescription(data.getDescription());
            updatedComponentLogic.setCout(data.getCout());
            return ComponentLogicRepository.save(updatedComponentLogic);
        } else {
            throw new NotFoundException("ComponentLogic not found");
        }

    }

    @Override
    public void delete(Long id) {
        Optional<ComponentLogicEntity> ComponentLogic = ComponentLogicRepository.findById(id);
        if (ComponentLogic.isPresent()) {
            ComponentLogicRepository.deleteById(id);
        } else {
            throw new NotFoundException("ComponentLogic not exist");
        }
    }
}
