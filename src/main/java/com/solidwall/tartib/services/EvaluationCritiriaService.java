package com.solidwall.tartib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.evaluationcritiria.CreateDto;
import com.solidwall.tartib.entities.EvaluationComponentEntity;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;
import com.solidwall.tartib.implementations.EvaluationCritiriaImplementation;
import com.solidwall.tartib.repositories.EvaluationComponentRepository;
import com.solidwall.tartib.repositories.EvaluationCritiriaRepository;

@Service
public class EvaluationCritiriaService implements EvaluationCritiriaImplementation {
    
    @Autowired
    private EvaluationCritiriaRepository critiriaRepository;
    
    @Autowired
    private EvaluationComponentRepository componentRepository;

    @Override
    public List<EvaluationCritiriaEntity> findAll() {
        List<EvaluationCritiriaEntity> critirias = critiriaRepository.findAll();
        if (critirias.isEmpty()) {
            throw new NotFoundException("No evaluation critirias found");
        }
        return critirias;
    }

    @Override
    public List<EvaluationCritiriaEntity> findByComponentId(Long componentId) {
        List<EvaluationCritiriaEntity> critirias = critiriaRepository.findByEvaluationComponentId(componentId);
        if (critirias.isEmpty()) {
            throw new NotFoundException("No critirias found for component: " + componentId);
        }
        return critirias;
    }

    @Override
    public EvaluationCritiriaEntity getOne(Long id) {
        return critiriaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation critiria not found"));
    }

    @Override
    public EvaluationCritiriaEntity create(CreateDto data) {
        EvaluationComponentEntity component = componentRepository.findById(data.getEvaluationComponentId())
            .orElseThrow(() -> new NotFoundException("Evaluation component not found"));
            
        EvaluationCritiriaEntity critiria = new EvaluationCritiriaEntity();
        critiria.setName(data.getName());
        critiria.setDescription(data.getDescription());
        critiria.setActive(data.isActive());
        critiria.setEvaluationComponent(component);
        return critiriaRepository.save(critiria);
    }



    @Override
    public void delete(Long id) {
        EvaluationCritiriaEntity critiria = getOne(id);
        critiriaRepository.delete(critiria);
    }
}