package com.solidwall.tartib.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.rolePermission.CreateDto;
import com.solidwall.tartib.dto.rolePermission.UpdateDto;
import com.solidwall.tartib.entities.AccessEntity;
import com.solidwall.tartib.entities.PermissionEntity;
import com.solidwall.tartib.entities.RoleAccessEntity;
import com.solidwall.tartib.entities.RoleEntity;
import com.solidwall.tartib.entities.RolePermissionEntity;
import com.solidwall.tartib.implementations.RolePermissionImplementation;
import com.solidwall.tartib.repositories.PermissionRepository;
import com.solidwall.tartib.repositories.RolePermissionRepository;
import com.solidwall.tartib.repositories.RoleRepository;

@Service
public class RolePermissionService implements RolePermissionImplementation{

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<RolePermissionEntity> findAll() {
        if (!rolePermissionRepository.findAll().isEmpty()) {
            return rolePermissionRepository.findAll();
        } else {
            throw new NotFoundException("not exist any permission role ");
        }
    }

    @Override
    public RolePermissionEntity getOne(Long id) {
        Optional<RolePermissionEntity> rolePermission = rolePermissionRepository.findById(id);
        if (rolePermission.isPresent()) {
            return rolePermission.get();
        } else {
            throw new NotFoundException("permission role not exist");
        }
    }

    @Override
    public RolePermissionEntity findOne(Map<String, String> data) {
        return null;
    }

    @Override
    public List<RolePermissionEntity> create(CreateDto data) {

        Optional<RoleEntity> role = roleRepository.findById(data.getRole());

        if (!role.isPresent()) {
            throw new NotFoundException("Role not found");
        }
        
        List<RolePermissionEntity> savedRolePermission = new ArrayList<RolePermissionEntity>();

        for (Long permissionId : data.getPermission()) {

            Optional<PermissionEntity> permission = permissionRepository.findById(permissionId);
            

            if (!permission.isPresent()) {
                throw new NotFoundException("Permission with ID " + permissionId + " not found");
            }

            Optional<RolePermissionEntity> rolePermission = rolePermissionRepository.findByPermissionAndRole(permission.get(), role.get());


            if (!rolePermission.isPresent()) {
                RolePermissionEntity newRolePermission = new RolePermissionEntity();
                newRolePermission.setPermission(permission.get());
                newRolePermission.setRole(role.get());
    
                savedRolePermission.add(rolePermissionRepository.save(newRolePermission));
            }

        }

        return savedRolePermission;

    }

    @Override
    public List<RolePermissionEntity> update(Long id, UpdateDto data) {

        Optional<RoleEntity> role = roleRepository.findById(data.getRole());

        if (!role.isPresent()) {
            throw new NotFoundException("Role not found");
        }
        
        List<RolePermissionEntity> savedRolePermission = new ArrayList<RolePermissionEntity>();

        for (Long permissionId : data.getPermission()) {

            Optional<PermissionEntity> permission = permissionRepository.findById(permissionId);
            

            if (!permission.isPresent()) {
                throw new NotFoundException("Permission with ID " + permissionId + " not found");
            }

            Optional<RolePermissionEntity> rolePermission = rolePermissionRepository.findByPermissionAndRole(permission.get(), role.get());


            if (!rolePermission.isPresent()) {
                RolePermissionEntity newRolePermission = new RolePermissionEntity();
                newRolePermission.setPermission(permission.get());
                newRolePermission.setRole(role.get());
    
                savedRolePermission.add(rolePermissionRepository.save(newRolePermission));
            }

        }

        return savedRolePermission;

    }

    @Override
    public void delete(Long id) {
        Optional<RolePermissionEntity> rolePermission = rolePermissionRepository.findById(id);
        if (rolePermission.isPresent()) {
            rolePermissionRepository.deleteById(id);
        } else {
            throw new NotFoundException("permission role not exist");
        }
    }

}
