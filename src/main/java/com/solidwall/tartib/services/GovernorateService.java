package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.implementations.GovernorateImplementation;
import com.solidwall.tartib.repositories.DistrictRepository;
import com.solidwall.tartib.repositories.GovernorateRepository;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.governorate.CreateDto;
import com.solidwall.tartib.dto.governorate.UpdateDto;

@Service
public class GovernorateService implements GovernorateImplementation {

  @Autowired
  GovernorateRepository governorateRepository;

  @Autowired
  DistrictRepository districtRepository;

  @Override
  public List<GovernorateEntity> findAll(Map<String, String> data) {
    if (!governorateRepository.findAll().isEmpty()) {
      return governorateRepository.findAll();
    } else {
      throw new NotFoundException("not exist any governorate ");
    }
  }

  @Override
  public GovernorateEntity getOne(Long id) {
    Optional<GovernorateEntity> gouvernorat = governorateRepository.findById(id);
    if (gouvernorat.isPresent()) {
      return gouvernorat.get();
    } else {
      throw new NotFoundException("governorate not exist");
    }
  }

  @Override
  public GovernorateEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public GovernorateEntity create(CreateDto data) {
    Optional<GovernorateEntity> gouvernorat = governorateRepository.findByName(data.getName());
    Optional<DistrictEntity> district = districtRepository.findById(data.getDistrict());
    if (!gouvernorat.isPresent()) {

      GovernorateEntity newGovernorat = new GovernorateEntity();
      newGovernorat.setName(data.getName());
      newGovernorat.setCode(data.getCode());
      newGovernorat.setDescription(data.getDescription());
      newGovernorat.setDistrict(district.get());
      newGovernorat.setActive(data.isActive());

      return governorateRepository.save(newGovernorat);

    } else {
      throw new FoundException("governorate already exist");
    }
  }

  @Override
  public GovernorateEntity update(Long id, UpdateDto data) {
    Optional<GovernorateEntity> gouvernorat = governorateRepository.findById(id);
    Optional<DistrictEntity> district = districtRepository.findById(data.getDistrict());
    if (gouvernorat.isPresent()) {

      GovernorateEntity updateGovernorat = gouvernorat.get();
      updateGovernorat.setName(data.getName());
      updateGovernorat.setCode(data.getCode());
      updateGovernorat.setDescription(data.getDescription());
      updateGovernorat.setActive(data.isActive());
      updateGovernorat.setDistrict(district.get());

      return governorateRepository.save(updateGovernorat);

    } else {
      throw new NotFoundException("governorate not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<GovernorateEntity> gouvernorat = governorateRepository.findById(id);
    if (gouvernorat.isPresent()) {
      governorateRepository.deleteById(id);
    } else {
      throw new NotFoundException("governorate not exist");
    }
  }

  @Override
  public List<GovernorateEntity> findByDistrict(Long id_district) {
    Optional<DistrictEntity> district = districtRepository.findById(id_district);
    if(district.isPresent()){
      if (!governorateRepository.findByDistrict(district.get()).isEmpty()) {
        return governorateRepository.findByDistrict(district.get());
      } else {
        throw new NotFoundException("not exist any governorate ");
      }
    }
    else {
      throw new NotFoundException("not exist any district ");
    }
   
  }
}
