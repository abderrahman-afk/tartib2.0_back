package com.solidwall.tartib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.evaluationnorme.CreateDto;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;
import com.solidwall.tartib.entities.EvaluationNormeEntity;
import com.solidwall.tartib.implementations.EvaluationNormeImplementation;
import com.solidwall.tartib.repositories.EvaluationIndicateurRepository;
import com.solidwall.tartib.repositories.EvaluationNormeRepository;

@Service
public class EvaluationNormeService implements EvaluationNormeImplementation {
    
    @Autowired
    private EvaluationNormeRepository normeRepository;
    
    @Autowired
    private EvaluationIndicateurRepository indicateurRepository;

    @Override
    public List<EvaluationNormeEntity> findAll() {
        List<EvaluationNormeEntity> normes = normeRepository.findAll();
        if (normes.isEmpty()) {
            throw new NotFoundException("No evaluation normes found");
        }
        return normes;
    }

    @Override
    public List<EvaluationNormeEntity> findByIndicateurId(Long indicateurId) {
        List<EvaluationNormeEntity> normes = normeRepository.findByEvaluationIndicateurId(indicateurId);
        if (normes.isEmpty()) {
            throw new NotFoundException("No normes found for indicateur: " + indicateurId);
        }
        return normes;
    }

    @Override
    public EvaluationNormeEntity getOne(Long id) {
        return normeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Evaluation norme not found"));
    }

    @Override
    public EvaluationNormeEntity create(CreateDto data) {
        EvaluationIndicateurEntity indicateur = indicateurRepository.findById(data.getEvaluationIndicateurId())
            .orElseThrow(() -> new NotFoundException("Evaluation indicateur not found"));
            
        EvaluationNormeEntity norme = new EvaluationNormeEntity();
        norme.setName(data.getName());
        norme.setActive(data.isActive());
        norme.setEvaluationIndicateur(indicateur);
        norme.setNote(data.getNote());
        return normeRepository.save(norme);
    }


    @Override
    public void delete(Long id) {
        EvaluationNormeEntity norme = getOne(id);
        normeRepository.delete(norme);
    }
}