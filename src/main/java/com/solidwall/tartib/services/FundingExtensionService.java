package com.solidwall.tartib.services;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.FundingExtensionEntity;
import com.solidwall.tartib.implementations.FundingExtensionImplementation;
import com.solidwall.tartib.repositories.FundingExtensionRepository;

@Service
public class FundingExtensionService implements FundingExtensionImplementation {

  @Autowired
  FundingExtensionRepository fundingExtensionRepository;

  @Override
  public List<FundingExtensionEntity> findAll() {
    if (!fundingExtensionRepository.findAll().isEmpty()) {
      return fundingExtensionRepository.findAll();
    } else {
      throw new NotFoundException("not exist any funding extension ");
    }
  }

  @Override
  public FundingExtensionEntity findOne() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public FundingExtensionEntity getOne(Long id) {
    Optional<FundingExtensionEntity> fundingExtension = fundingExtensionRepository.findById(id);
    if (fundingExtension.isPresent()) {
      return fundingExtension.get();
    } else {
      throw new NotFoundException("funding extension not exist");
    }
  }

  @Override
  public FundingExtensionEntity create(FundingExtensionEntity data) {
    Optional<FundingExtensionEntity> fundingExtension = fundingExtensionRepository.findByName(data.getName());
    if (!fundingExtension.isPresent()) {
      return fundingExtensionRepository.save(data);
    } else {
      throw new FoundException("funding extension already exist");
    }
  }

  @Override
  public FundingExtensionEntity update(Long id, FundingExtensionEntity data) {
    Optional<FundingExtensionEntity> fundingExtension = fundingExtensionRepository.findById(id);
    if (fundingExtension.isPresent()) {
      FundingExtensionEntity updateFundingExtension = fundingExtension.get();
      updateFundingExtension.setName(data.getName());
      updateFundingExtension.setDescription(data.getDescription());
      updateFundingExtension.setActualDate(data.getActualDate());
      updateFundingExtension.setStartDate(data.getStartDate());
      updateFundingExtension.setEndDate(data.getEndDate());
      updateFundingExtension.setSequence(data.getSequence());
      return fundingExtensionRepository.save(updateFundingExtension);
    } else {
      throw new NotFoundException("funding extension not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<FundingExtensionEntity> fundingExtension = fundingExtensionRepository.findById(id);
    if (fundingExtension.isPresent()) {
      fundingExtensionRepository.deleteById(id);
    } else {
      throw new NotFoundException("funding source not exist");
    }
  }

}
