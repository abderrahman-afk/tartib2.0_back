package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.permission.CreateDto;
import com.solidwall.tartib.dto.permission.UpdateDto;
import com.solidwall.tartib.entities.PermissionEntity;
import com.solidwall.tartib.implementations.PermissionImplementation;
import com.solidwall.tartib.repositories.PermissionRepository;

@Service
public class PermissionService implements PermissionImplementation{

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public List<PermissionEntity> findAll() {
        if (!permissionRepository.findAll().isEmpty()) {
            return permissionRepository.findAll();
        } else {
            throw new NotFoundException("not exist any permission");
        }
    }

    @Override
    public PermissionEntity findOne(Map<String, String> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public PermissionEntity getOne(Long id) {
        Optional<PermissionEntity> permission = permissionRepository.findById(id);
        if (permission.isPresent()) {
        return permission.get();
        } else {
        throw new NotFoundException("permission not exist");
        }
    }

    @Override
    public PermissionEntity create(CreateDto data) {
        PermissionEntity newPermission = new PermissionEntity();
        newPermission.setDescription(data.getDescription());
        newPermission.setName(data.getName());
        newPermission.setValue(data.getValue());
        newPermission.setActive(data.isActive());
        return permissionRepository.save(newPermission);
    }

    @Override
    public PermissionEntity update(Long id, UpdateDto data) {
        Optional<PermissionEntity> access = permissionRepository.findById(id);
        if (access.isPresent()) {
            PermissionEntity updatePermission = access.get();
            updatePermission.setName(data.getName());
            updatePermission.setValue(data.getValue());
            updatePermission.setDescription(data.getDescription());
            updatePermission.setActive(data.isActive());
            return permissionRepository.save(updatePermission);
        } else {
        throw new NotFoundException("permission not found");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<PermissionEntity> permission = permissionRepository.findById(id);
        if (permission.isPresent()) {
            permissionRepository.deleteById(id);
        } else {
        throw new NotFoundException("permission not exist");
        }
    }

}
