package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.FundingSourceEntity;
import com.solidwall.tartib.implementations.FundingSourceImplementation;
import com.solidwall.tartib.repositories.FundingSourceRepository;

@Service
public class FundingSourceService implements FundingSourceImplementation {

  @Autowired
  FundingSourceRepository fundingSourceRepository;

  @Override
  public List<FundingSourceEntity> findAll() {
    if (!fundingSourceRepository.findAll().isEmpty()) {
      return fundingSourceRepository.findAll();
    } else {
      throw new NotFoundException("not exist any funding source ");
    }
  }

  @Override
  public FundingSourceEntity getOne(Long id) {
    Optional<FundingSourceEntity> data = fundingSourceRepository.findById(id);
    if (data.isPresent()) {
      return data.get();
    } else {
      throw new NotFoundException("funding source not exist");
    }
  }

  @Override
  public FundingSourceEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public FundingSourceEntity create(FundingSourceEntity data) {
    Optional<FundingSourceEntity> fundingS = fundingSourceRepository.findByName(data.getName());
    if (!fundingS.isPresent()) {
      return fundingSourceRepository.save(data);
    } else {
      throw new FoundException("funding source already exist");
    }
  }

  @Override
  public FundingSourceEntity update(Long id, FundingSourceEntity data) {
    Optional<FundingSourceEntity> fundingS = fundingSourceRepository.findById(id);
    if (fundingS.isPresent()) {
      FundingSourceEntity updatedData = fundingS.get();
      updatedData.setName(data.getName());
      updatedData.setCode(data.getCode());
      updatedData.setDescription(data.getDescription());
      updatedData.setActive(data.isActive());
      updatedData.setUpdatedAt(data.getUpdatedAt());
      return fundingSourceRepository.save(updatedData);
    } else {
      throw new NotFoundException("funding source not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<FundingSourceEntity> data = fundingSourceRepository.findById(id);
    if (data.isPresent()) {
      fundingSourceRepository.deleteById(id);
    } else {
      throw new NotFoundException("funding source not exist");
    }
  }

}
