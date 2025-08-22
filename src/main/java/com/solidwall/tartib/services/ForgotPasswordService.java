package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.user.CreateForgotPasswordRequestDto;
import com.solidwall.tartib.dto.user.ForgotPasswordRequestDto;
import com.solidwall.tartib.dto.user.ProcessForgotPasswordRequestDto;
import com.solidwall.tartib.entities.ForgotPasswordRequestEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.enums.ForgotPasswordStatus;
import com.solidwall.tartib.implementations.ForgotPasswordImplementation;
import com.solidwall.tartib.repositories.ForgotPasswordRequestRepository;
import com.solidwall.tartib.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ForgotPasswordService implements ForgotPasswordImplementation {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ForgotPasswordRequestRepository forgotPasswordRequestRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity verifyEmailAndGetUser(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("No account found with this email address");
        }
    }

    @Override
    @Transactional
    public ForgotPasswordRequestDto createForgotPasswordRequest(CreateForgotPasswordRequestDto data) {
        // Verify email exists and get user
        UserEntity user = verifyEmailAndGetUser(data.getEmail());
        
        // Check if user already has a pending request
        Optional<ForgotPasswordRequestEntity> existingRequest = 
            forgotPasswordRequestRepository.findByUserAndStatus(user, ForgotPasswordStatus.PENDING);
        
        if (existingRequest.isPresent()) {
            throw new BadRequestException("You already have a pending password reset request");
        }
        
        // Create new request
        ForgotPasswordRequestEntity newRequest = new ForgotPasswordRequestEntity();
        newRequest.setUser(user);
        newRequest.setEmail(data.getEmail());
        newRequest.setStatus(ForgotPasswordStatus.PENDING);
        
        ForgotPasswordRequestEntity savedRequest = forgotPasswordRequestRepository.save(newRequest);
        return mapToDto(savedRequest);
    }

    @Override
    public List<ForgotPasswordRequestDto> getAllRequests() {
        List<ForgotPasswordRequestEntity> requests = forgotPasswordRequestRepository.findAll();
        if (requests.isEmpty()) {
            throw new NotFoundException("No forgot password requests found");
        }
        return requests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ForgotPasswordRequestDto> getRequestsByStatus(ForgotPasswordStatus status) {
        List<ForgotPasswordRequestEntity> requests = forgotPasswordRequestRepository.findByStatus(status);
        return requests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ForgotPasswordRequestDto getRequestById(Long id) {
        Optional<ForgotPasswordRequestEntity> request = forgotPasswordRequestRepository.findById(id);
        if (request.isPresent()) {
            return mapToDto(request.get());
        } else {
            throw new NotFoundException("Forgot password request not found");
        }
    }

    @Override
    @Transactional
    public ForgotPasswordRequestDto processRequest(Long id, ProcessForgotPasswordRequestDto data) {
        ForgotPasswordRequestEntity request = forgotPasswordRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        
        if (request.getStatus() != ForgotPasswordStatus.PENDING) {
            throw new BadRequestException("Request has already been processed");
        }
        
        // Get current admin user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = authentication.getName();
        
        request.setStatus(data.getStatus());
        request.setProcessedBy(adminUsername);
        request.setAdminNotes(data.getAdminNotes());
        
        // CRITICAL: If approved and new password provided, update user password immediately
        if (data.getStatus() == ForgotPasswordStatus.APPROVED && data.getNewPassword() != null && !data.getNewPassword().trim().isEmpty()) {
            UserEntity user = request.getUser();
            // Hash the new password before saving
            user.setPassword(passwordEncoder.encode(data.getNewPassword()));
            userRepository.save(user);
            
            // Log password change for security audit
            System.out.println("Password changed for user: " + user.getUsername() + " by admin: " + adminUsername);
        } else if (data.getStatus() == ForgotPasswordStatus.APPROVED) {
            throw new BadRequestException("New password is required when approving a password reset request");
        }
        
        ForgotPasswordRequestEntity savedRequest = forgotPasswordRequestRepository.save(request);
        return mapToDto(savedRequest);
    }

    @Override
    public List<ForgotPasswordRequestDto> getUserRequests(String email) {
        List<ForgotPasswordRequestEntity> requests = 
            forgotPasswordRequestRepository.findByEmailOrderByCreatedAtDesc(email);
        return requests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ForgotPasswordRequestDto mapToDto(ForgotPasswordRequestEntity entity) {
        ForgotPasswordRequestDto dto = new ForgotPasswordRequestDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setUserFirstname(entity.getUser().getFirstname());
        dto.setUserLastname(entity.getUser().getLastname());
        dto.setUsername(entity.getUser().getUsername());
        dto.setStatus(entity.getStatus());
        dto.setProcessedBy(entity.getProcessedBy());
        dto.setAdminNotes(entity.getAdminNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}