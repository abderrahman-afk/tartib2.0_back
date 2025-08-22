package com.solidwall.tartib.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.financialsource.*; 
import com.solidwall.tartib.entities.FinancialSourceEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.implementations.FinancialSourceImplementation;
import com.solidwall.tartib.repositories.FinancialSourceRepository;
import com.solidwall.tartib.repositories.ProjectPlanRepository;

@Service
public class FinancialSourceService implements FinancialSourceImplementation {
        @Autowired
    FinancialSourceRepository financialSourceRepository;

    @Autowired
    ProjectPlanRepository projectPlanRepository;

    @Override
    public List<FinancialSourceEntity> findAll() {
        if (!financialSourceRepository.findAll().isEmpty()) {
            return financialSourceRepository.findAll();
        } else {
            throw new NotFoundException("not exist any FinancialSource");
        }
    }

    @Override
    public FinancialSourceEntity getOne(Long id) {
        Optional<FinancialSourceEntity> FinancialSource = financialSourceRepository.findById(id);
        if (FinancialSource.isPresent()) {
            return FinancialSource.get();
        } else {
            throw new NotFoundException("FinancialSource not exist");
        }
    }

    @Override
    public FinancialSourceEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public FinancialSourceEntity create(CreateDto data) {
        Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findById(data.getProjectPlan_id());
        if (projectPlan.isPresent()) {
            Optional<FinancialSourceEntity> FinancialSource = financialSourceRepository.findByType(data.getType());
            if (!FinancialSource.isPresent()) {
                FinancialSourceEntity newFinancialSource = new FinancialSourceEntity();
                newFinancialSource.setBailleur(data.getBailleur());
         
                newFinancialSource.setDevise(data.getDevise());
                newFinancialSource.setMontant(data.getMontant());
                newFinancialSource.setTauxEchange(data.getTauxEchange());
                newFinancialSource.setStatut(data.getStatut());
                newFinancialSource.setMontantDinars(data.getMontantDinars());
                newFinancialSource.setType(data.getType());

                return financialSourceRepository.save(newFinancialSource);
            } else {
                throw new FoundException("FinancialSource already exist");
            }

        } else {
            throw new FoundException("there is no project plan to link this FinancialSource");
        }

    }

    @Override
    public FinancialSourceEntity update(Long id, UpdateDto data) {

        Optional<FinancialSourceEntity> FinancialSource = financialSourceRepository.findById(id);

        if (FinancialSource.isPresent()) {
            FinancialSourceEntity updatedFinancialSource = FinancialSource.get();
            updatedFinancialSource.setBailleur(data.getBailleur());
            updatedFinancialSource.setDevise(data.getDevise());
            updatedFinancialSource.setMontant(data.getMontant());
            updatedFinancialSource.setTauxEchange(data.getTauxEchange());
            updatedFinancialSource.setStatut(data.getStatut());
            updatedFinancialSource.setMontantDinars(data.getMontantDinars());
            updatedFinancialSource.setType(data.getType());
            return financialSourceRepository.save(updatedFinancialSource);
        } else {
            throw new NotFoundException("FinancialSource not found");
        }

    }

    @Override
    public void delete(Long id) {
        Optional<FinancialSourceEntity> FinancialSource = financialSourceRepository.findById(id);
        if (FinancialSource.isPresent()) {
            financialSourceRepository.deleteById(id);
        } else {
            throw new NotFoundException("FinancialSource not exist");
        }
    }
}
