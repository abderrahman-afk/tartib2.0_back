package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.StrategyAxisEntity;
import com.solidwall.tartib.implementations.StrategyAxisImplementation;
import com.solidwall.tartib.repositories.StrategyAxisRepository;

@Service
public class StrategyAxisService implements StrategyAxisImplementation {

    @Autowired
    StrategyAxisRepository strategyAxisRepository;

    @Override
    public StrategyAxisEntity getOne(Long id) {
        Optional<StrategyAxisEntity> data = strategyAxisRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("strategy axis not exist");
        }
    }

    @Override
    public StrategyAxisEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<StrategyAxisEntity> findAll() {
        if (!strategyAxisRepository.findAll().isEmpty()) {
            return strategyAxisRepository.findAll();
        } else {
            throw new NotFoundException("not exist any strategy axis ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<StrategyAxisEntity> data = strategyAxisRepository.findById(id);
        if (data.isPresent()) {
            strategyAxisRepository.deleteById(id);
        } else {
            throw new NotFoundException("strategy axis not exist");
        }
    }

    @Override
    public StrategyAxisEntity create(StrategyAxisEntity data) {
        Optional<StrategyAxisEntity> strategyAxis = strategyAxisRepository.findByName(data.getName());
        if (!strategyAxis.isPresent()) {
            return strategyAxisRepository.save(data);
        } else {
            throw new FoundException("strategy axis already exist");
        }
    }

    @Override
    public StrategyAxisEntity update(Long id, StrategyAxisEntity data) {
        Optional<StrategyAxisEntity> strategyAxis = strategyAxisRepository.findById(id);
        if (strategyAxis.isPresent()) {
            StrategyAxisEntity updateStrategyAxis = strategyAxis.get();
            updateStrategyAxis.setName(data.getName());
            updateStrategyAxis.setCode(data.getCode());
            updateStrategyAxis.setDescription(data.getDescription());
            updateStrategyAxis.setActive(data.isActive());
            updateStrategyAxis.setUpdatedAt(data.getUpdatedAt());
            return strategyAxisRepository.save(updateStrategyAxis);
        } else {
            throw new NotFoundException("strategy axis not found");
        }
    }

}
