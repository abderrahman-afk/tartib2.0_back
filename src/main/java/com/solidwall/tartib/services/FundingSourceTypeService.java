package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.FundingSourceTypeEntity;
import com.solidwall.tartib.implementations.FundingSourceTypeImplementation;
import com.solidwall.tartib.repositories.FundingSourceTypeRepository;

@Service
public class FundingSourceTypeService implements FundingSourceTypeImplementation {

  @Autowired
  FundingSourceTypeRepository fundingSourceTypeRepository;

  @Override
  public List<FundingSourceTypeEntity> findAll() {
    if (!fundingSourceTypeRepository.findAll().isEmpty()) {
      return fundingSourceTypeRepository.findAll();
    } else {
      throw new NotFoundException("not exist any funding source type ");
    }
  }

  @Override
  public FundingSourceTypeEntity getOne(Long id) {
    Optional<FundingSourceTypeEntity> fundingSourceType = fundingSourceTypeRepository.findById(id);
    if (fundingSourceType.isPresent()) {
      return fundingSourceType.get();
    } else {
      throw new NotFoundException("funding source type not exist");
    }
  }

  @Override
  public FundingSourceTypeEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public FundingSourceTypeEntity create(FundingSourceTypeEntity data) {
    Optional<FundingSourceTypeEntity> fundingSourceType = fundingSourceTypeRepository.findByName(data.getName());
    if (!fundingSourceType.isPresent()) {
      return fundingSourceTypeRepository.save(data);
    } else {
      throw new FoundException("funding source type already exist");
    }
  }

  @Override
  public FundingSourceTypeEntity update(Long id, FundingSourceTypeEntity data) {
    Optional<FundingSourceTypeEntity> fundingSourceType = fundingSourceTypeRepository.findById(id);
    if (fundingSourceType.isPresent()) {
      FundingSourceTypeEntity updateFundingSourceType = fundingSourceType.get();
      updateFundingSourceType.setName(data.getName());
      updateFundingSourceType.setCode(data.getCode());
      updateFundingSourceType.setDescription(data.getDescription());
      updateFundingSourceType.setActive(data.isActive());
      updateFundingSourceType.setUpdatedAt(data.getUpdatedAt());
      return fundingSourceTypeRepository.save(updateFundingSourceType);
    } else {
      throw new NotFoundException("funding source type not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<FundingSourceTypeEntity> fundingSourceType = fundingSourceTypeRepository.findById(id);
    if (fundingSourceType.isPresent()) {
      fundingSourceTypeRepository.deleteById(id);
    } else {
      throw new NotFoundException("funding source type source not exist");
    }
  }

}
