package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.EconomicNatureEntity;
import com.solidwall.tartib.implementations.EconomicNatureImplementation;
import com.solidwall.tartib.repositories.EconomicNatureRepository;

@Service
public class EconomicNatureService implements EconomicNatureImplementation {

    @Autowired
    EconomicNatureRepository economicNatureRepository;

    @Override
    public EconomicNatureEntity getOne(Long id) {
        Optional<EconomicNatureEntity> data = economicNatureRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("economic nature not exist");
        }
    }

    @Override
    public EconomicNatureEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<EconomicNatureEntity> findAll() {
        if (!economicNatureRepository.findAll().isEmpty()) {
            return economicNatureRepository.findAll();
        } else {
            throw new NotFoundException("not exist any economic nature ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<EconomicNatureEntity> data = economicNatureRepository.findById(id);
        if (data.isPresent()) {
            economicNatureRepository.deleteById(id);
        } else {
            throw new NotFoundException("economic nature not exist");
        }
    }

    @Override
    public EconomicNatureEntity create(EconomicNatureEntity data) {
        Optional<EconomicNatureEntity> economic = economicNatureRepository.findByName(data.getName());
        if (!economic.isPresent()) {
            return economicNatureRepository.save(data);
        } else {
            throw new FoundException("economic nature already exist");
        }
    }

    @Override
    public EconomicNatureEntity update(Long id, EconomicNatureEntity data) {
        Optional<EconomicNatureEntity> economic = economicNatureRepository.findById(id);
        if (economic.isPresent()) {
            EconomicNatureEntity updatedEco = economic.get();
            updatedEco.setName(data.getName());
            updatedEco.setCode(data.getCode());
            updatedEco.setDescription(data.getDescription());
            updatedEco.setActive(data.isActive());
            updatedEco.setUpdatedAt(data.getUpdatedAt());
            return economicNatureRepository.save(updatedEco);
        } else {
            throw new NotFoundException("economic nature not found");
        }
    }
}
