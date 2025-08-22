package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.deanship.CreateDto;
import com.solidwall.tartib.dto.deanship.UpdateDto;
import com.solidwall.tartib.entities.DeanshipEntity;
import com.solidwall.tartib.entities.DelegationEntity;
import com.solidwall.tartib.implementations.DeanshipsImplementation;
import com.solidwall.tartib.repositories.DeanshipRepository;
import com.solidwall.tartib.repositories.DelegationRepository;
import com.solidwall.tartib.repositories.DistrictRepository;
import com.solidwall.tartib.repositories.GovernorateRepository;

@Service
public class DeanshipsService implements DeanshipsImplementation {

  @Autowired
  DeanshipRepository deanshipRepository;

  @Autowired
  DelegationRepository delegationRepository;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  GovernorateRepository governorateRepository;

  @Override
  public DeanshipEntity getOne(Long id) {
    Optional<DeanshipEntity> data = deanshipRepository.findById(id);
    if (data.isPresent()) {
      return data.get();
    } else {
      throw new NotFoundException("deanship not exist");
    }
  }

  @Override
  public DeanshipEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public List<DeanshipEntity> findAll() {
    if (!deanshipRepository.findAll().isEmpty()) {
      return deanshipRepository.findAll();
    } else {
      throw new NotFoundException("not exist any deanShips ");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<DeanshipEntity> data = deanshipRepository.findById(id);
    if (data.isPresent()) {
      deanshipRepository.deleteById(id);
    } else {
      throw new NotFoundException("deanShips not exist");
    }
  }

  @Override
  public DeanshipEntity create(CreateDto data) {

    Optional<DeanshipEntity> deanShips = deanshipRepository.findByName(data.getName());
    Optional<DelegationEntity> delegation = delegationRepository.findById(data.getDelegation());

    if (!deanShips.isPresent()) {
      DeanshipEntity newDeanship = new DeanshipEntity();
      newDeanship.setName(data.getName());
      newDeanship.setCode(data.getCode());
      newDeanship.setDescription(data.getDescription());
      newDeanship.setActive(data.isActive());
      newDeanship.setDelegation(delegation.get());
      return deanshipRepository.save(newDeanship);
    } else {
      throw new FoundException("deanShips already exist");
    }
  }

  @Override
  public DeanshipEntity update(Long id, UpdateDto data) {

    Optional<DeanshipEntity> deanShip = deanshipRepository.findById(id);
    Optional<DelegationEntity> delegation = delegationRepository.findById(data.getDelegation());

    if (deanShip.isPresent()) {
      DeanshipEntity updateDeanship = deanShip.get();
      updateDeanship.setName(data.getName());
      updateDeanship.setCode(data.getCode());
      updateDeanship.setDescription(data.getDescription());
      updateDeanship.setActive(data.isActive());
      updateDeanship.setDelegation(delegation.get());
      return deanshipRepository.save(updateDeanship);
    } else {
      throw new NotFoundException("deanShip not found");
    }
  }


  @Override
  public List<DeanshipEntity> findByDelegation(Long id) {
    Optional<DelegationEntity> delegation = delegationRepository.findById(id);
    if(delegation.isPresent()){
      if (!deanshipRepository.findByDelegation(delegation.get()).isEmpty()) {
        return deanshipRepository.findByDelegation(delegation.get());
      } else {
        throw new NotFoundException("not exist any deanship ");
      }
    }
    else {
      throw new NotFoundException("not exist any delegation ");
    }
    
  }
}
