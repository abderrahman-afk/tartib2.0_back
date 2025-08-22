package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.cnapValidationReserve.CreateDto;
import com.solidwall.tartib.dto.cnapValidationReserve.UpdateDto;
import com.solidwall.tartib.entities.CnapValidationReserveEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.implementations.CnapValidationReserveImplementation;
import com.solidwall.tartib.repositories.CnapValidationReserveRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CnapValidationReserveService implements CnapValidationReserveImplementation{

    @Autowired
    private CnapValidationReserveRepository cnapValidationReserveRepository;
    @Autowired
    private ProjectIdentityRepository projectIdentityRepository;
    @Override
    public List<CnapValidationReserveEntity> findAll() {
        return cnapValidationReserveRepository.findAll();
    }
    @Override
    public CnapValidationReserveEntity findOne(Map<String, String> data) {
        if (data.get("projectIdentityId") != null  ) {
            return cnapValidationReserveRepository.findByProjectIdentityId(
                    Long.valueOf(data.get("projectIdentityId")))
                .orElseThrow(() -> new NotFoundException("CnapValidationReserve not found"));
        }
        throw new BadRequestException("Invalid parameters");
    }
    @Override
    public CnapValidationReserveEntity getOne(Long id) {
        return cnapValidationReserveRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("DeroCnapValidationReservegation not found"));

    }
    @Override
    public CnapValidationReserveEntity create(CreateDto data) {
 ProjectIdentityEntity projectIdentity = projectIdentityRepository
            .findById(data.getProjectIdentityId())
            .orElseThrow(() -> new NotFoundException("Project identity not found"));

            CnapValidationReserveEntity entity =  new CnapValidationReserveEntity();
        entity.setProjectIdentity(projectIdentity);
        entity.setReserve(data.getReserve());
        return cnapValidationReserveRepository.save(entity);
    }
    @Override
    public CnapValidationReserveEntity update(Long id, UpdateDto data) {
 
        CnapValidationReserveEntity entity = cnapValidationReserveRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CnapValidationReserve not found"));

        ProjectIdentityEntity projectIdentity = projectIdentityRepository
                .findById(data.getProjectIdentityId())
                .orElseThrow(() -> new NotFoundException("Project identity not found"));

        entity.setProjectIdentity(projectIdentity);
        entity.setReserve(data.getReserve());
        return cnapValidationReserveRepository.save(entity);
    }
    @Override
    public void delete(Long id) {
        CnapValidationReserveEntity entity = cnapValidationReserveRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CnapValidationReserve not found"));
        cnapValidationReserveRepository.delete(entity);
    }
    @Override
    public CnapValidationReserveEntity findByProjectIdentityId(Long projectIdentityId) {
        return cnapValidationReserveRepository.findByProjectIdentityId(projectIdentityId)
                .orElseThrow(() -> new NotFoundException("CnapValidationReserve not found"));
    }

    @Transactional
    @Override
    public CnapValidationReserveEntity updateReserve(Long projectIdentityId, String reserve) {
        CnapValidationReserveEntity entity = cnapValidationReserveRepository.findByProjectIdentityId(projectIdentityId)
                .orElseThrow(() -> new NotFoundException("CnapValidationReserve not found"));

        entity.setReserve(reserve);
        return cnapValidationReserveRepository.save(entity);
    }
}
