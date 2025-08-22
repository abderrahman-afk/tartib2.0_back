package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.role.RoleDto;
import com.solidwall.tartib.dto.userrole.CreateDto;
import com.solidwall.tartib.dto.userrole.UpdateDto;
import com.solidwall.tartib.dto.userrole.UserRoleDto;
import com.solidwall.tartib.entities.AccessEntity;
import com.solidwall.tartib.entities.RoleAccessEntity;
import com.solidwall.tartib.entities.RoleEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.entities.UserRoleEntity;
import com.solidwall.tartib.implementations.UserRoleImplementation;
import com.solidwall.tartib.repositories.RoleRepository;
import com.solidwall.tartib.repositories.UserRepository;
import com.solidwall.tartib.repositories.UserRoleRepository;

@Service
public class UserRoleService implements UserRoleImplementation {

  @Autowired
  UserRoleRepository userRoleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Override
  public List<UserRoleDto> findAll() {
    if (!userRoleRepository.findAll().isEmpty()) {
      return userRoleRepository.findAll().stream()
          .map(this::mapToDto)
          .collect(Collectors.toList());
    } else {
      throw new NotFoundException("no user roles exist");
    }
  }

  @Override
  public UserRoleDto getOne(Long id) {
    Optional<UserRoleEntity> userRole = userRoleRepository.findById(id);
    if (userRole.isPresent()) {
      return mapToDto(userRole.get());
    } else {
      throw new NotFoundException("user role not exist");
    }
  }

  @Override
  public UserRoleDto findOne() {
    return null;
  }

  @Override
  @Transactional
  public UserRoleDto create(CreateDto data) {
    Optional<UserEntity> userOptional = userRepository.findById(data.getUser());
    Optional<RoleEntity> roleOptional = roleRepository.findById(data.getRole());
    
    if (!userOptional.isPresent()) {
      throw new NotFoundException("User not found");
    }
    if (!roleOptional.isPresent()) {
      throw new NotFoundException("Role not found");
    }
    
    UserEntity user = userOptional.get();
    RoleEntity role = roleOptional.get();
    
    // Check if the user already has any role assignment
    List<UserRoleEntity> existingUserRoles = userRoleRepository.findByUser(user);
    
    if (!existingUserRoles.isEmpty()) {
      // Update the existing role instead of creating a new one
      UserRoleEntity existingUserRole = existingUserRoles.get(0);
      existingUserRole.setRole(role);
      UserRoleEntity savedEntity = userRoleRepository.save(existingUserRole);
      UserRoleDto dto = mapToDto(savedEntity);
      // Update ministry access type if provided
      if (data.getMinistryAccessType() != null) {
        user.setMinistryAccessType(data.getMinistryAccessType());
        userRepository.save(user);
      }
      return dto;
    } else {
      // Create a new user role assignment
         if (data.getMinistryAccessType() != null) {
        UserEntity userEntity = userOptional.get();
        userEntity.setMinistryAccessType(data.getMinistryAccessType());
        userRepository.save(userEntity);
      }
      UserRoleEntity newUserRole = new UserRoleEntity();
      newUserRole.setUser(user);
      newUserRole.setRole(role);
      UserRoleEntity savEntity=  userRoleRepository.save(newUserRole);
      UserRoleDto dto = mapToDto(savEntity);
      return dto;
    }
  }

  @Override
  @Transactional
  public UserRoleDto update(Long id, UpdateDto data) {
    Optional<UserRoleEntity> userRoleOptional = userRoleRepository.findById(id);
    
    if (!userRoleOptional.isPresent()) {
      throw new NotFoundException("User role not found");
    }
      
    UserRoleEntity userRole = userRoleOptional.get();
    
    Optional<UserEntity> userOptional = userRepository.findById(data.getUser());
    Optional<RoleEntity> roleOptional = roleRepository.findById(data.getRole());
    
    if (!userOptional.isPresent()) {
      throw new NotFoundException("User not found");
    }
    if (!roleOptional.isPresent()) {
      throw new NotFoundException("Role not found");
    }
     if (data.getMinistryAccessType() != null) {
        UserEntity userEntity = userOptional.get();
        userEntity.setMinistryAccessType(data.getMinistryAccessType());
        userRepository.save(userEntity);
      }
    userRole.setUser(userOptional.get());
    userRole.setRole(roleOptional.get());
    
    UserRoleEntity savedEntity = userRoleRepository.save(userRole);
    UserRoleDto dto = mapToDto(savedEntity);
    return dto;
  }

  @Override
  public void delete(Long id) {
    Optional<UserRoleEntity> userRole = userRoleRepository.findById(id);
    if (userRole.isPresent()) {
      userRoleRepository.deleteById(id);
    } else {
      throw new NotFoundException("user role not exist");
    }
  }

    // Mapping function from Entity to DTO
  private UserRoleDto mapToDto(UserRoleEntity userRole) {
    UserRoleDto dto = new UserRoleDto();
    dto.setId(userRole.getId());
    
    // Basic user info
    UserEntity user = userRole.getUser();
    dto.setUserId(user.getId());
    dto.setUsername(user.getUsername());
        if (user.getMinistryAccessType() != null) {
        dto.setMinistryAccessType(user.getMinistryAccessType().toString());
    }
    // Map role using the RoleDto to avoid recursion
    RoleEntity role = userRole.getRole();
    RoleDto roleDto = new RoleDto();
    roleDto.setId(role.getId());
    roleDto.setName(role.getName());
    roleDto.setDescription(role.getDescription());
    roleDto.setActive(role.isActive());
    
    // Extract access entities directly from roleAccess relationship
    if (role.getRoleAccess() != null) {
      List<AccessEntity> accesses = role.getRoleAccess().stream()
          .map(RoleAccessEntity::getAccess)
          .collect(Collectors.toList());
      roleDto.setAccess(accesses);
    } else {
      roleDto.setAccess(new ArrayList<>());
    }
    
    dto.setRole(roleDto);
    
    return dto;
  }

  @Override
  public UserRoleDto getByUserId(Long id) {
    // TODO Auto-generated method stub
    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User not found"));
    List<UserRoleEntity> userRole = userRoleRepository.findByUser(user);
    if (!userRole.isEmpty()) {
      return mapToDto(userRole.get(0));
    } else {
      throw new NotFoundException("User role not found");
    }
  }

}