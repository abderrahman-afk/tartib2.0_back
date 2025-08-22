package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.access.CreateDto;
import com.solidwall.tartib.dto.access.UpdateDto;
import com.solidwall.tartib.entities.AccessEntity;
import com.solidwall.tartib.entities.ActivityEntity;
import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.implementations.AccessImplementation;
import com.solidwall.tartib.repositories.AccessRepository;

@Service
public class AccessService implements AccessImplementation{

    @Autowired
    AccessRepository accessRepository;

    @Override
    public List<AccessEntity> findAll() {
        if (!accessRepository.findAll().isEmpty()) {
            return accessRepository.findAll();
        } else {
            throw new NotFoundException("not exist any access");
        }
    }

    @Override
    public AccessEntity findOne(Map<String, String> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public AccessEntity getOne(Long id) {
        Optional<AccessEntity> access = accessRepository.findById(id);
        if (access.isPresent()) {
        return access.get();
        } else {
        throw new NotFoundException("access not exist");
        }
    }

    @Override
    public AccessEntity create(CreateDto data) {
        AccessEntity newAccess = new AccessEntity();
        newAccess.setDescription(data.getDescription());
        newAccess.setName(data.getName());
        newAccess.setValue(data.getValue());
        newAccess.setActive(data.isActive());
        return accessRepository.save(newAccess);
    }

    @Override
    public AccessEntity update(Long id, UpdateDto data) {
        Optional<AccessEntity> access = accessRepository.findById(id);
        if (access.isPresent()) {
            AccessEntity updateAccess = access.get();
            updateAccess.setName(data.getName());
            updateAccess.setValue(data.getValue());
            updateAccess.setDescription(data.getDescription());
            updateAccess.setActive(data.isActive());
            return accessRepository.save(updateAccess);
        } else {
        throw new NotFoundException("access not found");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<AccessEntity> activity = accessRepository.findById(id);
        if (activity.isPresent()) {
            accessRepository.deleteById(id);
        } else {
        throw new NotFoundException("access not exist");
        }
    }

}
