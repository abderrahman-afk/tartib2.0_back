package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.BudgetSectionEntity;
import com.solidwall.tartib.implementations.BudgetSectionImplementation;
import com.solidwall.tartib.repositories.BudgetSectionRepository;

@Service
public class BudgetSectionService implements BudgetSectionImplementation {

    @Autowired
    BudgetSectionRepository budgetSectionRepository;

    @Override
    public BudgetSectionEntity getOne(Long id) {
        Optional<BudgetSectionEntity> data = budgetSectionRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("budget section not exist");
        }
    }

    @Override
    public BudgetSectionEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<BudgetSectionEntity> findAll() {
        if (!budgetSectionRepository.findAll().isEmpty()) {
            return budgetSectionRepository.findAll();
        } else {
            throw new NotFoundException("not exist any budget section ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<BudgetSectionEntity> data = budgetSectionRepository.findById(id);
        if (data.isPresent()) {
            budgetSectionRepository.deleteById(id);
        } else {
            throw new NotFoundException("budget section not exist");
        }
    }

    @Override
    public BudgetSectionEntity create(BudgetSectionEntity data) {
        Optional<BudgetSectionEntity> budget = budgetSectionRepository.findByName(data.getName());
        if (!budget.isPresent()) {
            return budgetSectionRepository.save(data);
        } else {
            throw new FoundException("budget section already exist");
        }
    }

    @Override
    public BudgetSectionEntity update(Long id, BudgetSectionEntity data) {
        Optional<BudgetSectionEntity> budget = budgetSectionRepository.findById(id);
        if (budget.isPresent()) {
            BudgetSectionEntity updatedBudget = budget.get();
            updatedBudget.setName(data.getName());
            updatedBudget.setCode(data.getCode());
            updatedBudget.setDescription(data.getDescription());
            updatedBudget.setActive(data.isActive());
            updatedBudget.setUpdatedAt(data.getUpdatedAt());
            return budgetSectionRepository.save(updatedBudget);
        } else {
            throw new NotFoundException("budget section not found");
        }
    }
}
