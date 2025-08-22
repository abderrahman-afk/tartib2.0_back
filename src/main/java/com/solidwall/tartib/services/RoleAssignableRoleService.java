package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.roleAssignableRole.CreateDto;
import com.solidwall.tartib.dto.roleAssignableRole.UpdateDto;
import com.solidwall.tartib.entities.RoleAssignableRoleEntity;
import com.solidwall.tartib.entities.RoleEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.entities.UserRoleEntity;
import com.solidwall.tartib.implementations.RoleAssignableRoleImplementation;
import com.solidwall.tartib.repositories.RoleAssignableRoleRepository;
import com.solidwall.tartib.repositories.RoleRepository;
import com.solidwall.tartib.repositories.UserRepository;

@Service
public class RoleAssignableRoleService implements RoleAssignableRoleImplementation {

    @Autowired
    private RoleAssignableRoleRepository roleAssignableRoleRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<RoleAssignableRoleEntity> findAll() {
        List<RoleAssignableRoleEntity> roleAssignableRoles = roleAssignableRoleRepository.findAll();
        if (roleAssignableRoles.isEmpty()) {
            throw new NotFoundException("No role assignable roles found");
        }
        return roleAssignableRoles;
    }

    @Override
    public List<RoleAssignableRoleEntity> findByRoleId(Long roleId) {
        Optional<RoleEntity> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw new NotFoundException("Role not found");
        }
        
        List<RoleAssignableRoleEntity> roleAssignableRoles = roleAssignableRoleRepository.findByRole(roleOptional.get());
        return roleAssignableRoles;
    }
    
    @Override
    public List<Long> getAssignableRoleIds(Long roleId) {
        Optional<RoleEntity> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw new NotFoundException("Role not found");
        }
        
        List<Long> assignableRoleIds = roleAssignableRoleRepository.findByRole(roleOptional.get()).stream()
            .map(rar -> rar.getAssignableRole().getId())
            .collect(Collectors.toList());
        
        return assignableRoleIds;
    }

@Override
@Transactional
public List<RoleAssignableRoleEntity> create(CreateDto data) {
    Optional<RoleEntity> roleOptional = roleRepository.findById(data.getRole());
    if (!roleOptional.isPresent()) {
        throw new NotFoundException("Role not found");
    }
    
    RoleEntity role = roleOptional.get();
    List<RoleAssignableRoleEntity> result = new ArrayList<>();
    
    try {
        // Delete existing relationships first
        roleAssignableRoleRepository.deleteByRoleId(data.getRole());
        
        // Create new relationships
        if (data.getAssignableRoles() != null && !data.getAssignableRoles().isEmpty()) {
            for (Long assignableRoleId : data.getAssignableRoles()) {
                Optional<RoleEntity> assignableRoleOptional = roleRepository.findById(assignableRoleId);
                if (!assignableRoleOptional.isPresent()) {
                    continue; // Skip invalid roles
                }
                
                RoleEntity assignableRole = assignableRoleOptional.get();
                
                RoleAssignableRoleEntity entity = new RoleAssignableRoleEntity();
                entity.setRole(role);
                entity.setAssignableRole(assignableRole);
                
                result.add(roleAssignableRoleRepository.save(entity));
            }
        }
        
        return result;
    } catch (Exception e) {
        // Log the error
        System.err.println("Error creating role assignable roles: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}

@Override
@Transactional
public List<RoleAssignableRoleEntity> update(Long roleId, UpdateDto data) {
    if (!roleId.equals(data.getRole())) {
        throw new IllegalArgumentException("Role ID in path and body must match");
    }
    
    // Make sure to delete existing relationships first
    roleAssignableRoleRepository.deleteByRoleId(roleId);
    
    // Then create new relationships
    return create(new CreateDto() {{
        setRole(data.getRole());
        setAssignableRoles(data.getAssignableRoles());
    }});
}

 

    @Override
    public void delete(Long id) {
        if (!roleAssignableRoleRepository.existsById(id)) {
            throw new NotFoundException("Role assignable role not found");
        }
        roleAssignableRoleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByRoleId(Long roleId) {
        Optional<RoleEntity> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw new NotFoundException("Role not found");
        }
        
        roleAssignableRoleRepository.deleteByRole(roleOptional.get());
    }
    
    @Override
    public List<Long> getAssignableRoleIdsForCurrentUser() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Find the user
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        
        // Get the user's roles
        List<RoleEntity> userRoles = user.getUserRoles().stream()
                .map(UserRoleEntity::getRole)
                .collect(Collectors.toList());
        
        // Get the assignable role IDs for each of the user's roles
        Set<Long> assignableRoleIds = new HashSet<>();
        for (RoleEntity role : userRoles) {
            List<RoleAssignableRoleEntity> roleAssignableRoles = roleAssignableRoleRepository.findByRole(role);
            assignableRoleIds.addAll(roleAssignableRoles.stream()
                .map(rar -> rar.getAssignableRole().getId())
                .collect(Collectors.toSet()));
        }
        
        return new ArrayList<>(assignableRoleIds);
    }
}