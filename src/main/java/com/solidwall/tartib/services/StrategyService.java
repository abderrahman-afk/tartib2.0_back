package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.StrategyEntity;
import com.solidwall.tartib.implementations.StrategyImplementation;
import com.solidwall.tartib.repositories.StrategyRepository;

@Service
public class StrategyService implements StrategyImplementation {

    @Autowired
    StrategyRepository strategyRepository;

    @Override
    public StrategyEntity getOne(Long id) {
        Optional<StrategyEntity> data = strategyRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("strategy not exist");
        }
    }

    @Override
    public StrategyEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<StrategyEntity> findAll() {
        if (!strategyRepository.findAll().isEmpty()) {
            return strategyRepository.findAll();
        } else {
            throw new NotFoundException("not exist any strategy ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<StrategyEntity> data = strategyRepository.findById(id);
        if (data.isPresent()) {
            strategyRepository.deleteById(id);
        } else {
            throw new NotFoundException("strategy not exist");
        }
    }

    @Override
    public StrategyEntity create(StrategyEntity data) {
        Optional<StrategyEntity> strategy = strategyRepository.findByName(data.getName());
        if (!strategy.isPresent()) {
            return strategyRepository.save(data);
        } else {
            throw new FoundException("strategy already exist");
        }
    }

    @Override
    public StrategyEntity update(Long id, StrategyEntity data) {
        Optional<StrategyEntity> strategy = strategyRepository.findById(id);
        if (strategy.isPresent()) {
            StrategyEntity updatedStrategy = strategy.get();
            updatedStrategy.setName(data.getName());
            updatedStrategy.setCode(data.getCode());
            updatedStrategy.setDescription(data.getDescription());
            updatedStrategy.setActive(data.isActive());
            updatedStrategy.setUpdatedAt(data.getUpdatedAt());
            return strategyRepository.save(updatedStrategy);
        } else {
            throw new NotFoundException("strategy not found");
        }
    }
}
