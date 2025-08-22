package com.solidwall.tartib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.evaluationcomponent.CreateDto;
import com.solidwall.tartib.entities.EvaluationComponentEntity;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.implementations.EvaluationComponentImplementation;
import com.solidwall.tartib.repositories.EvaluationComponentRepository;
import com.solidwall.tartib.repositories.EvaluationGridRepository;

@Service
public class EvaluationComponentService implements EvaluationComponentImplementation {
    
    @Autowired
    private EvaluationComponentRepository componentRepository;
    
    @Autowired
    private EvaluationGridRepository gridRepository;

    @Override
    public List<EvaluationComponentEntity> findAll() {
        List<EvaluationComponentEntity> components = componentRepository.findAll();
        if (components.isEmpty()) {
            throw new NotFoundException("No evaluation components found");
        }
        return components;
    }

    @Override
    public List<EvaluationComponentEntity> findByGridId(Long gridId) {
        List<EvaluationComponentEntity> components = componentRepository.findByEvaluationGridId(gridId);
        if (components.isEmpty()) {
            throw new NotFoundException("No components found for grid: " + gridId);
        }
        return components;
    }

    @Override
    public EvaluationComponentEntity getOne(Long id) {
        return componentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation component not found"));
    }

    @Override
    public EvaluationComponentEntity create(CreateDto data) {
        EvaluationGridEntity grid = gridRepository.findById(data.getEvaluationGridId())
            .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));
            
        EvaluationComponentEntity component = new EvaluationComponentEntity();
        component.setName(data.getName());
        component.setDescription(data.getDescription());
        component.setActive(data.isActive());
        component.setEvaluationGrid(grid);
        return componentRepository.save(component);
    }
 
    @Override
    public void delete(Long id) {
        EvaluationComponentEntity component = getOne(id);
        componentRepository.delete(component);
    }
}

