package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.autorisation.CreateDto;
import com.solidwall.tartib.dto.autorisation.UpdateDto;
import com.solidwall.tartib.entities.AutorisationEntity;
import com.solidwall.tartib.implementations.AutorisationImplementation;
import com.solidwall.tartib.repositories.AutorisationRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectStudyRepository;

public class AutorisationService implements AutorisationImplementation {

    @Autowired
    AutorisationRepository autorisationRepository;
    @Autowired
    ProjectStudyRepository projectStudyRepository;
    @Autowired
    ProjectIdentityRepository projectIdentityRepository;

    @Override
    public List<AutorisationEntity> findAll() {
              if (!autorisationRepository.findAll().isEmpty()) {
            return autorisationRepository.findAll();
        } else {
            throw new NotFoundException("not exist any autorisation");
        }
    }

    @Override
    public AutorisationEntity getOne(Long id) {
             Optional<AutorisationEntity> data = autorisationRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("autorisation not exist");
        }
    }

    @Override
    public AutorisationEntity findOne(Map<String, String> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public AutorisationEntity create(CreateDto data) {
        return null;
  
    }

    @Override
    public AutorisationEntity update(Long id, UpdateDto data) {
        return null;
    
 
    }

    @Override
    public void delete(Long id) {
        Optional<AutorisationEntity> data = autorisationRepository.findById(id);
        if (data.isPresent()) {
            autorisationRepository.deleteById(id);
        } else {
            throw new NotFoundException("Autorisation not exist");
        }
    }

    
  
  }
  
