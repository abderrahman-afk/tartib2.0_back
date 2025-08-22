package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.delegation.CreateDto;
import com.solidwall.tartib.dto.delegation.UpdateDto;
import com.solidwall.tartib.entities.DelegationEntity;
import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.implementations.DelegationImplementation;
import com.solidwall.tartib.repositories.DelegationRepository;
import com.solidwall.tartib.repositories.DistrictRepository;
import com.solidwall.tartib.repositories.GovernorateRepository;

@Service
public class DelegationsService implements DelegationImplementation {

  @Autowired
  DelegationRepository delegationRepository;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  GovernorateRepository governorateRepository;

  @Override
  public List<DelegationEntity> findAll() {
    if (!delegationRepository.findAll().isEmpty()) {
      return delegationRepository.findAll();
    } else {
      throw new NotFoundException("not exist any delegation");
    }
  }

  @Override
  public DelegationEntity getOne(Long id) {
    Optional<DelegationEntity> delegation = delegationRepository.findById(id);
    if (delegation.isPresent()) {
      return delegation.get();
    } else {
      throw new NotFoundException("delegation not exist");
    }
  }

  @Override
  public DelegationEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public DelegationEntity create(CreateDto data) {

    Optional<DelegationEntity> delegation = delegationRepository.findByName(data.getName());
    Optional<GovernorateEntity> governorate = governorateRepository.findById(data.getGovernorate());

    if (!delegation.isPresent()) {
      DelegationEntity newDelegation = new DelegationEntity();
      newDelegation.setName(data.getName());
      newDelegation.setCode(data.getCode());
      newDelegation.setDescription(data.getDescription());
      newDelegation.setGovernorate(governorate.get());
      newDelegation.setActive(data.isActive());
      return delegationRepository.save(newDelegation);
    } else {
      throw new FoundException("delegation already exist");
    }

   
  }

  @Override
  public DelegationEntity update(Long id, UpdateDto data) {

    Optional<DelegationEntity> delegation = delegationRepository.findById(id);
    Optional<GovernorateEntity> governorat = governorateRepository.findById(data.getGovernorate());
    
    if (delegation.isPresent()) {
      DelegationEntity updatedDelegation = delegation.get();
      updatedDelegation.setName(data.getName());
      updatedDelegation.setCode(data.getCode());
      updatedDelegation.setDescription(data.getDescription());
      updatedDelegation.setGovernorate(governorat.get());
      updatedDelegation.setActive(data.isActive());
      return delegationRepository.save(updatedDelegation);
    } else {
      throw new NotFoundException("delegation not found");
    }
    
  }

  @Override
  public void delete(Long id) {
    Optional<DelegationEntity> delegation = delegationRepository.findById(id);
    if (delegation.isPresent()) {
      delegationRepository.deleteById(id);
    } else {
      throw new NotFoundException("delegation not exist");
    }
  }

  @Override
  public List<DelegationEntity> findByGovernorat(Long id) {
    Optional<GovernorateEntity> governorat = governorateRepository.findById(id);
    if(governorat.isPresent()){
      if (!delegationRepository.findByGovernorate(governorat.get()).isEmpty()) {
        return delegationRepository.findByGovernorate(governorat.get());
      } else {
        throw new NotFoundException("not exist any delegation ");
      }
    }
    else {
      throw new NotFoundException("not exist any governorat ");
    }
    
  }

  
}
