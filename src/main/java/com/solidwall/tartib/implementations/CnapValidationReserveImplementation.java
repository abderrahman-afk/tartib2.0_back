package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.cnapValidationReserve.CreateDto;
import com.solidwall.tartib.dto.cnapValidationReserve.UpdateDto;

import com.solidwall.tartib.entities.CnapValidationReserveEntity;

public interface CnapValidationReserveImplementation {

    List<CnapValidationReserveEntity> findAll();

    CnapValidationReserveEntity findOne(Map<String, String> data);

    CnapValidationReserveEntity getOne(Long id);

    CnapValidationReserveEntity create(CreateDto data);

    CnapValidationReserveEntity update(Long id, UpdateDto data);

    void delete(Long id);

    CnapValidationReserveEntity findByProjectIdentityId(Long projectIdentityId);

    CnapValidationReserveEntity updateReserve(Long projectIdentityId, String reserve);    

}
