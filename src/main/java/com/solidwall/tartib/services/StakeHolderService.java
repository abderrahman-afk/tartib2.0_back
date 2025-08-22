package com.solidwall.tartib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.ProjectZoneEntity;
import com.solidwall.tartib.entities.StakeHolderEntity;
import com.solidwall.tartib.implementations.StakeHolderImplementation;
import com.solidwall.tartib.repositories.ProjectZoneRepository;
import com.solidwall.tartib.repositories.StakeholderRepository;
import com.solidwall.tartib.dto.stakeholder.CreateDto;
import com.solidwall.tartib.dto.stakeholder.UpdateDto;
import java.util.Optional;


@Service
public class StakeHolderService implements StakeHolderImplementation{

  @Autowired
    StakeholderRepository StakeHolderRepository;
    
  @Autowired
  ProjectZoneRepository projectZoneRepository;

    @Override
    public List<StakeHolderEntity> findAll() {
        if (!StakeHolderRepository.findAll().isEmpty()) {
            return StakeHolderRepository.findAll();
        } else {
            throw new NotFoundException("not exist any StakeHolder");
        }
    }

    @Override
    public StakeHolderEntity getOne(Long id) {
        Optional<StakeHolderEntity> StakeHolder = StakeHolderRepository.findById(id);
        if (StakeHolder.isPresent()) {
            return StakeHolder.get();
        } else {
            throw new NotFoundException("StakeHolder not exist");
        }
    }

    @Override
    public StakeHolderEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public StakeHolderEntity create(CreateDto data) {
        Optional<ProjectZoneEntity> projectZone = projectZoneRepository.findById(data.getProjectZone_id());
        if(projectZone.isPresent()){
            Optional<StakeHolderEntity> StakeHolder = StakeHolderRepository.findByName(data.getName());
            if (!StakeHolder.isPresent()) {
                StakeHolderEntity newStakeHolder = new StakeHolderEntity();
                newStakeHolder.setProjectZone(projectZone.get());
                newStakeHolder.setName(data.getName());
                newStakeHolder.setDescription(data.getDescription());
                newStakeHolder.setType(data.getType());
                return StakeHolderRepository.save(newStakeHolder);
            } else {
                throw new FoundException("StakeHolder already exist");
            }

        }
        else {
            throw new FoundException("there is no project zone to link this stakeholder");
        }

    }

    @Override
    public StakeHolderEntity update(Long id, UpdateDto data) {

        Optional<StakeHolderEntity> StakeHolder = StakeHolderRepository.findById(id);

        if (StakeHolder.isPresent()) {
            StakeHolderEntity updatedStakeHolder = StakeHolder.get();
            updatedStakeHolder.setName(data.getName());
            updatedStakeHolder.setDescription(data.getDescription());
            updatedStakeHolder.setType(data.getType());
            return StakeHolderRepository.save(updatedStakeHolder);
        } else {
            throw new NotFoundException("StakeHolder not found");
        }

    }

    @Override
    public void delete(Long id) {
        Optional<StakeHolderEntity> StakeHolder = StakeHolderRepository.findById(id);
        if (StakeHolder.isPresent()) {
            StakeHolderRepository.deleteById(id);
        } else {
            throw new NotFoundException("StakeHolder not exist");
        }
    }
 
}
