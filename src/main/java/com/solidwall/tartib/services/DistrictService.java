package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.implementations.DistrictImplementation;
import com.solidwall.tartib.repositories.DistrictRepository;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;

@Service
public class DistrictService implements DistrictImplementation {

  @Autowired
  DistrictRepository districtRepository;

  @Override
  public DistrictEntity getOne(Long id) {
    Optional<DistrictEntity> district = districtRepository.findById(id);
    if (district.isPresent()) {
      return district.get();
    } else {
      throw new NotFoundException("district not exist");
    }
  }

  @Override
  public DistrictEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public List<DistrictEntity> findAll() {
    if (!districtRepository.findAll().isEmpty()) {
      return districtRepository.findAll();
    } else {
      throw new NotFoundException("not exist any district ");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<DistrictEntity> district = districtRepository.findById(id);
    if (district.isPresent()) {
      districtRepository.deleteById(id);
    } else {
      throw new NotFoundException("district not exist");
    }
  }

  @Override
  public DistrictEntity create(DistrictEntity data) {
    Optional<DistrictEntity> district = districtRepository.findByName(data.getName());
    if (!district.isPresent()) {
      return districtRepository.save(data);
    } else {
      throw new FoundException("district already exist");
    }
  }

  @Override
  public DistrictEntity update(Long id, DistrictEntity data) {
    Optional<DistrictEntity> district = districtRepository.findById(id);
    if (district.isPresent()) {
      DistrictEntity updatedDistrict = district.get();
      updatedDistrict.setName(data.getName());
      updatedDistrict.setCode(data.getCode());
      updatedDistrict.setDescription(data.getDescription());
      updatedDistrict.setActive(data.isActive());
      updatedDistrict.setUpdatedAt(data.getUpdatedAt());
      return districtRepository.save(updatedDistrict);
    } else {
      throw new NotFoundException("district not found");
    }
  }

  @Override
  public String getNameById(Long id) {
    // TODO Auto-generated method stub
    Optional<DistrictEntity> district = districtRepository.findById(id);
    if (district.isPresent()) {
      return district.get().getName();
    } else {
      throw new NotFoundException("district not exist");
    }
}

}
