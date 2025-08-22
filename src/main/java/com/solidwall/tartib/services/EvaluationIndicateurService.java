package com.solidwall.tartib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.evaluationindicateur.CreateDto;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;
import com.solidwall.tartib.implementations.EvaluationIndicateurImplementation;
import com.solidwall.tartib.repositories.EvaluationCritiriaRepository;
import com.solidwall.tartib.repositories.EvaluationIndicateurRepository;

@Service
public class EvaluationIndicateurService implements EvaluationIndicateurImplementation {
    
    @Autowired
    private EvaluationIndicateurRepository indicateurRepository;
    
    @Autowired
    private EvaluationCritiriaRepository critiriaRepository;

    @Override
    public List<EvaluationIndicateurEntity> findAll() {
        List<EvaluationIndicateurEntity> indicateurs = indicateurRepository.findAll();
        if (indicateurs.isEmpty()) {
            throw new NotFoundException("No evaluation indicateurs found");
        }
        return indicateurs;
    }

    @Override
    public List<EvaluationIndicateurEntity> findByCritiriaId(Long critiriaId) {
        List<EvaluationIndicateurEntity> indicateurs = indicateurRepository.findByEvaluationCritiriaId(critiriaId);
        if (indicateurs.isEmpty()) {
            throw new NotFoundException("No indicateurs found for critiria: " + critiriaId);
        }
        return indicateurs;
    }

    @Override
    public EvaluationIndicateurEntity getOne(Long id) {
        return indicateurRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation indicateur not found"));
    }

    @Override
    public EvaluationIndicateurEntity create(CreateDto data) {
        EvaluationCritiriaEntity critiria = critiriaRepository.findById(data.getEvaluationCritiriaId())
            .orElseThrow(() -> new NotFoundException("Evaluation critiria not found"));
            
        EvaluationIndicateurEntity indicateur = new EvaluationIndicateurEntity();
        indicateur.setName(data.getName());
        indicateur.setDescription(data.getDescription());
        indicateur.setActive(data.isActive());
        indicateur.setEvaluationCritiria(critiria);
        return indicateurRepository.save(indicateur);
    }


    @Override
    public void delete(Long id) {
        EvaluationIndicateurEntity indicateur = getOne(id);
        indicateurRepository.delete(indicateur);
    }
}