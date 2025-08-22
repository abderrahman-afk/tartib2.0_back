package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.role.RoleDto;
import com.solidwall.tartib.entities.AccessEntity;
import com.solidwall.tartib.entities.RoleAccessEntity;
import com.solidwall.tartib.entities.RoleEntity;
import com.solidwall.tartib.implementations.RoleImplementation;
import com.solidwall.tartib.repositories.RoleRepository;

@Service
public class RoleService implements RoleImplementation {

  @Autowired
  RoleRepository roleRepository;

  @Override
  public List<RoleDto> findAll() {
    List<RoleEntity> roles = roleRepository.findAll();
    if (roles.isEmpty()) {
      throw new NotFoundException("no roles exist");
    }
    return roles.stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

 

  @Override
  public RoleDto getOne(Long id) {
    Optional<RoleEntity> role = roleRepository.findById(id);
    if (role.isPresent()) {
      return mapToDto(role.get());
    } else {
      throw new NotFoundException("role not exist");
    }
  }

  @Override
  public RoleDto findOne(String name) {
    RoleEntity role= roleRepository.findByName(name).get();
    if (role != null) {
      return mapToDto(role);
    } else {
      throw new NotFoundException("role not exist");
    }
  }

  @Override
  public RoleEntity create(RoleEntity data) {
    Optional<RoleEntity> role = roleRepository.findByName(data.getName());
    if (!role.isPresent()) {
      return roleRepository.save(data);
    } else {
      throw new FoundException("role exist");
    }
  }

  @Override
  public RoleEntity update(Long id, RoleEntity data) {
    Optional<RoleEntity> role = roleRepository.findById(id);
    if (role.isPresent()) {
      RoleEntity updateRole = role.get();
      updateRole.setName(data.getName());
      updateRole.setActive(data.isActive());
      updateRole.setCreatedAt(data.getCreatedAt());
      updateRole.setDescription(data.getDescription());
      updateRole.setUpdatedAt(data.getUpdatedAt());
      return roleRepository.save(updateRole);
    } else {
      throw new NotFoundException("role not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<RoleEntity> role = roleRepository.findById(id);
    if (role.isPresent()) {
      roleRepository.deleteById(id);
    } else {
      throw new NotFoundException("role not exist");
    }
  }
  
 private RoleDto mapToDto(RoleEntity role) {
    RoleDto dto = new RoleDto();
    dto.setId(role.getId());
    dto.setName(role.getName());
    dto.setDescription(role.getDescription());
    dto.setActive(role.isActive());
    
    // Extract access entities directly from roleAccess relationship
    if (role.getRoleAccess() != null) {
      List<AccessEntity> accesses = role.getRoleAccess().stream()
          .map(RoleAccessEntity::getAccess)
          .collect(Collectors.toList());
      dto.setAccess(accesses);
    } else {
      dto.setAccess(new ArrayList<>());
    }
    
    return dto;
  }
}
