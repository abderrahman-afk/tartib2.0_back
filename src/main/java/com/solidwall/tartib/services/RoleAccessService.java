package com.solidwall.tartib.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.roleAccess.CreateDto;
import com.solidwall.tartib.dto.roleAccess.FindDto;
import com.solidwall.tartib.dto.roleAccess.UpdateDto;
import com.solidwall.tartib.entities.AccessEntity;
import com.solidwall.tartib.entities.RoleAccessEntity;
import com.solidwall.tartib.entities.RoleEntity;
import com.solidwall.tartib.implementations.RoleAccessImplementation;
import com.solidwall.tartib.repositories.AccessRepository;
import com.solidwall.tartib.repositories.RoleAccessRepository;
import com.solidwall.tartib.repositories.RoleRepository;
import com.solidwall.tartib.repositories.abstractions.RoleAccessRepositoryAbstract;

import jakarta.transaction.Transactional;

@Service
public class RoleAccessService implements RoleAccessImplementation {

    @Autowired
    RoleAccessRepository roleAccessRepository;

    @Autowired
    RoleAccessRepositoryAbstract roleAccessRepositoryAbstract;

    @Autowired
    AccessRepository accessRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<RoleAccessEntity> findAll() {
        if (!roleAccessRepository.findAll().isEmpty()) {
            return roleAccessRepository.findAll();
        } else {
            throw new NotFoundException("not exist any access role ");
        }
    }

    @Override
    public RoleAccessEntity getOne(Long id) {
        Optional<RoleAccessEntity> roleAccess = roleAccessRepository.findById(id);
        if (roleAccess.isPresent()) {
            return roleAccess.get();
        } else {
            throw new NotFoundException("access role not exist");
        }
    }

    @Override
    public RoleAccessEntity findOne(FindDto data) {

        Optional<RoleAccessEntity> roleAccess = roleAccessRepositoryAbstract.findOneByCriteria(data);
        if (roleAccess.isPresent()) {
            return roleAccess.get();
        } else {
            return null; // or throw an exception if preferred
        }
    }

    @Override
    public List<RoleAccessEntity> create(CreateDto data) {


        Optional<RoleEntity> role = roleRepository.findById(data.getRole());

        if (!role.isPresent()) {
            throw new NotFoundException("Role not found");
        }
        
        List<RoleAccessEntity> savedAccessRoles = new ArrayList<RoleAccessEntity>();

        for (Long accessId : data.getAccess()) {

            Optional<AccessEntity> access = accessRepository.findById(accessId);
            

            if (!access.isPresent()) {
                throw new NotFoundException("Access with ID " + accessId + " not found");
            }

            Optional<RoleAccessEntity> roleAccess = roleAccessRepository.findByAccessAndRole(access.get(), role.get());


            if (!roleAccess.isPresent()) {
                RoleAccessEntity newAccessRole = new RoleAccessEntity();
                newAccessRole.setAccess(access.get());
                newAccessRole.setRole(role.get());
    
                savedAccessRoles.add(roleAccessRepository.save(newAccessRole));
            }

        }

        return savedAccessRoles;

    }

@Override
@Transactional
public List<RoleAccessEntity> update(Long id, UpdateDto data) {
    Optional<RoleEntity> role = roleRepository.findById(data.getRole());

    if (!role.isPresent()) {
        throw new NotFoundException("Role not found");
    }

    // **CLEAN APPROACH**: Delete all existing access for this role
    roleAccessRepository.deleteByRole(role.get());
    
    // Flush to ensure deletions are committed
    roleAccessRepository.flush();

    List<RoleAccessEntity> savedAccessRoles = new ArrayList<>();

    // Add the new access permissions
    for (Long accessId : data.getAccess()) {
        Optional<AccessEntity> access = accessRepository.findById(accessId);
        
        if (!access.isPresent()) {
            throw new NotFoundException("Access with ID " + accessId + " not found");
        }

        RoleAccessEntity newAccessRole = new RoleAccessEntity();
        newAccessRole.setAccess(access.get());
        newAccessRole.setRole(role.get());

        savedAccessRoles.add(roleAccessRepository.save(newAccessRole));
    }

    return savedAccessRoles;
}

    @Override
    public void delete(Long id) {
        Optional<RoleAccessEntity> roleAccess = roleAccessRepository.findById(id);
        if (roleAccess.isPresent()) {
            roleAccessRepository.deleteById(id);
        } else {
            throw new NotFoundException("access role not exist");
        }
    }

}
