package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.CreateUserDto;
import com.solidwall.tartib.dto.user.ChangePasswordDto;
import com.solidwall.tartib.dto.user.SimpleMinistryDto;
import com.solidwall.tartib.dto.user.SimpleRoleDto;
import com.solidwall.tartib.dto.user.SimpleUserRoleDto;
import com.solidwall.tartib.dto.user.UserProfileDto;
import com.solidwall.tartib.entities.MinisterEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.entities.UserRoleEntity;
import com.solidwall.tartib.enums.MinistryAccessType;
import com.solidwall.tartib.implementations.UserImplementation;
import com.solidwall.tartib.repositories.MinisterRepository;
import com.solidwall.tartib.repositories.RoleRepository;
import com.solidwall.tartib.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService implements UserImplementation {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;
 @Autowired
  MinisterRepository ministerRepository;
  @Override
  public UserEntity getOne(Long id) {
    Optional<UserEntity> user = userRepository.findById(id);
    if (user.isPresent()) {
      return user.get();
    } else {
      throw new NotFoundException("user not exist");
    }
  }

  @Override
  public UserEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public List<UserEntity> findAll() {
    if (!userRepository.findAll().isEmpty()) {
      return userRepository.findAll();
    } else {
      throw new NotFoundException("not exist any user ");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<UserEntity> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.deleteById(id);
    } else {
      throw new NotFoundException("user not exist");
    }
  }

//   @Override
//   public UserEntity create(UserEntity data) {
//     Optional<UserEntity> user = userRepository.findByEmail(data.getEmail());
//     //RoleEntity role = roleRepository.findById(1L).get();
    
//     if (!user.isPresent()) {
//       UserEntity newUser = new UserEntity();
//       newUser.setUsername(data.getUsername());
//       newUser.setFirstname(data.getFirstname());
//       newUser.setLastname(data.getLastname());
//       newUser.setEmail(data.getEmail());
//       newUser.setPassword(passwordEncoder.encode(data.getPassword()));

//       //newUser.setRoles(Collections.singletonList(role));
// // ADD MINISTRY ASSIGNMENT:
//       if (data.getMinistry() != null) {
//         newUser.setMinistry(data.getMinistry());
//       }

//       // ADD REFERENCE MINISTRIES:
//       if (data.getReferenceMinistries() != null && !data.getReferenceMinistries().isEmpty()) {
//         newUser.setReferenceMinistries(data.getReferenceMinistries());
//       }

//       // SET DEFAULT MINISTRY ACCESS TYPE:
//       newUser.setMinistryAccessType(
//         data.getMinistryAccessType() != null ? 
//         data.getMinistryAccessType() : 
//         MinistryAccessType.OWN_MINISTRY
//       );
//       return userRepository.save(newUser);
//     } else {
//       throw new FoundException("user exist");
//     }
//   }

//   @Override
//   public UserEntity update(Long id, UserEntity data) {
//     Optional<UserEntity> user = userRepository.findById(id);
//     if (user.isPresent()) {
//       UserEntity updateUser = user.get();
//       updateUser.setUsername(data.getUsername());
//       updateUser.setFirstname(data.getFirstname());
//       updateUser.setLastname(data.getLastname());
//       updateUser.setEmail(data.getEmail());
//       updateUser.setPassword(passwordEncoder.encode(data.getPassword()));

// // ADD MINISTRY ASSIGNMENT:
//       if (data.getMinistry() != null) {
//         updateUser.setMinistry(data.getMinistry());
//       }

//       // ADD REFERENCE MINISTRIES:
//       if (data.getReferenceMinistries() != null && !data.getReferenceMinistries().isEmpty()) {
//         updateUser.setReferenceMinistries(data.getReferenceMinistries());
//       }

//       // SET DEFAULT MINISTRY ACCESS TYPE:
//       updateUser.setMinistryAccessType(
//         data.getMinistryAccessType() != null ? 
//         data.getMinistryAccessType() : 
//         MinistryAccessType.OWN_MINISTRY
//       );


//       return userRepository.save(updateUser);
//     } else {
//       throw new NotFoundException("user not found");
//     }
//   }


   public UserEntity create(CreateUserDto data) {
    Optional<UserEntity> existingUser = userRepository.findByEmail(data.getEmail());
    
    if (!existingUser.isPresent()) {
      UserEntity newUser = new UserEntity();
      newUser.setUsername(data.getUsername());
      newUser.setFirstname(data.getFirstname());
      newUser.setLastname(data.getLastname());
      newUser.setEmail(data.getEmail());
      newUser.setPassword(passwordEncoder.encode(data.getPassword()));

      // SET MINISTRY:
      if (data.getMinistryId() != null) {
        MinisterEntity ministry = ministerRepository.findById(data.getMinistryId())
            .orElseThrow(() -> new NotFoundException("Ministry not found"));
        newUser.setMinistry(ministry);
      }

      // SET REFERENCE MINISTRIES:
      if (data.getReferenceMinistryIds() != null && !data.getReferenceMinistryIds().isEmpty()) {
        Set<MinisterEntity> referenceMinistries = new HashSet<>(
            ministerRepository.findAllById(data.getReferenceMinistryIds()));
        newUser.setReferenceMinistries(referenceMinistries);
      }

      // SET MINISTRY ACCESS TYPE:
      // newUser.setMinistryAccessType(
      //   data.getMinistryAccessType() != null ? 
      //   data.getMinistryAccessType() : 
      //   MinistryAccessType.OWN_MINISTRY
      // );

      return userRepository.save(newUser);
    } else {
      throw new FoundException("user exist");
    }
  }

  // ADD update method with DTO
  public UserEntity update(Long id, CreateUserDto data) {
    Optional<UserEntity> userOptional = userRepository.findById(id);
    
    if (userOptional.isPresent()) {
      UserEntity updateUser = userOptional.get();
      updateUser.setUsername(data.getUsername());
      updateUser.setFirstname(data.getFirstname());
      updateUser.setLastname(data.getLastname());
      updateUser.setEmail(data.getEmail());
      
      // Only update password if provided
      if (data.getPassword() != null && !data.getPassword().isEmpty()) {
        updateUser.setPassword(passwordEncoder.encode(data.getPassword()));
      }

      // UPDATE MINISTRY:
      if (data.getMinistryId() != null) {
        MinisterEntity ministry = ministerRepository.findById(data.getMinistryId())
            .orElseThrow(() -> new NotFoundException("Ministry not found"));
        updateUser.setMinistry(ministry);
      }

      // UPDATE REFERENCE MINISTRIES:
      if (data.getReferenceMinistryIds() != null) {
        if (data.getReferenceMinistryIds().isEmpty()) {
          updateUser.setReferenceMinistries(new HashSet<>());
        } else {
          Set<MinisterEntity> referenceMinistries = new HashSet<>(
              ministerRepository.findAllById(data.getReferenceMinistryIds()));
          updateUser.setReferenceMinistries(referenceMinistries);
        }
      }

      // UPDATE MINISTRY ACCESS TYPE:
      // if (data.getMinistryAccessType() != null) {
      //   updateUser.setMinistryAccessType(data.getMinistryAccessType());
      // }

      return userRepository.save(updateUser);
    } else {
      throw new NotFoundException("user not found");
    }
  }

  public UserProfileDto getUserProfile(UserEntity user) {
    UserProfileDto profile = new UserProfileDto();
    
    // Set basic user information
    profile.setId(user.getId());
    profile.setFirstname(user.getFirstname());
    profile.setLastname(user.getLastname());
    profile.setUsername(user.getUsername());
    profile.setEmail(user.getEmail());
    profile.setEnabled(user.isEnabled());
    profile.setCreatedAt(user.getCreatedAt());
    profile.setUpdatedAt(user.getUpdatedAt());
    profile.setMinistryAccessType(user.getMinistryAccessType());
    
    // Convert ministry to DTO
    SimpleMinistryDto ministryDto = null;
    if (user.getMinistry() != null) {
        ministryDto = new SimpleMinistryDto(
            user.getMinistry().getId(),
            user.getMinistry().getName(),
            user.getMinistry().getCode(),
            user.getMinistry().getDescription()
        );
    }
    profile.setMinistry(ministryDto);
    
    // Convert reference ministries to DTOs
    List<SimpleMinistryDto> referenceMinistryDtos = new ArrayList<>();
    if (user.getReferenceMinistries() != null && !user.getReferenceMinistries().isEmpty()) {
        for (MinisterEntity ministry : user.getReferenceMinistries()) {
            SimpleMinistryDto refMinistryDto = new SimpleMinistryDto(
                ministry.getId(),
                ministry.getName(),
                ministry.getCode(),
                ministry.getDescription()
            );
            referenceMinistryDtos.add(refMinistryDto);
        }
    }
    profile.setReferenceMinistries(referenceMinistryDtos);
    
    // Convert user roles to DTOs
    List<SimpleUserRoleDto> userRoleDtos = new ArrayList<>();
    if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
        for (var userRole : user.getUserRoles()) {
            if (userRole.getRole() != null) {
                SimpleRoleDto roleDto = new SimpleRoleDto(
                    userRole.getRole().getId(),
                    userRole.getRole().getName(),
                    userRole.getRole().getDescription(),
                    userRole.getRole().getCreatedAt(),
                    userRole.getRole().getUpdatedAt()
                );
                
                SimpleUserRoleDto userRoleDto = new SimpleUserRoleDto(
                    userRole.getId(),
                    roleDto,
                    userRole.getCreatedAt(),
                    userRole.getUpdatedAt()
                );
                userRoleDtos.add(userRoleDto);
            }
        }
    }
    profile.setUserRoles(userRoleDtos);
    
    // Extract permissions from roles
    List<String> allPermissions = new ArrayList<>();
    if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
        for (var userRole : user.getUserRoles()) {
            if (userRole.getRole() != null && userRole.getRole().getRoleAccess() != null) {
                for (var roleAccess : userRole.getRole().getRoleAccess()) {
                    if (roleAccess.getAccess() != null && roleAccess.getAccess().getValue() != null) {
                        String permissionValue = roleAccess.getAccess().getValue();
                        if (!allPermissions.contains(permissionValue)) {
                            allPermissions.add(permissionValue);
                        }
                    }
                }
            }
        }
    }
    profile.setPermissions(allPermissions);
    
    return profile;
  }
  public void changePassword(UserEntity user, ChangePasswordDto changePasswordDto) {
    UserRoleEntity userRole = user.getUserRoles().stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException("User role not found"));

        if(changePasswordDto.getNewPassword().toLowerCase().contains(user.getUsername().toLowerCase())) {
            throw new RuntimeException("le nouveau mot de passe ne peut pas contenir le nom d'utilisateur");
        }

        if(changePasswordDto.getNewPassword().toLowerCase().contains(userRole.getRole().getName().toLowerCase())) {
            throw new RuntimeException("le nouveau mot de passe ne peut pas contenir le rôle d'utilisateur");
        }

    // Validate current password
    if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
      throw new RuntimeException("le mot de passe actuel est incorrect");
    }
    
    // Validate new password confirmation
    if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
      throw new RuntimeException("le nouveau mot de passe et la confirmation ne correspondent pas");
    }
    
    // Validate new password is different from current
    if (passwordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
      throw new RuntimeException("le nouveau mot de passe doit être différent de l'ancien mot de passe");
    }
    
    // Update password
    user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
    userRepository.save(user);
  }
}
