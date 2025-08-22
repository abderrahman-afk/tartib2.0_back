package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.user.CreateForgotPasswordRequestDto;
import com.solidwall.tartib.dto.user.ForgotPasswordRequestDto;
import com.solidwall.tartib.dto.user.ProcessForgotPasswordRequestDto;
import com.solidwall.tartib.entities.ForgotPasswordRequestEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.enums.ForgotPasswordStatus;

public interface ForgotPasswordImplementation {
    
    UserEntity verifyEmailAndGetUser(String email);
    
    ForgotPasswordRequestDto createForgotPasswordRequest(CreateForgotPasswordRequestDto data);
    
    List<ForgotPasswordRequestDto> getAllRequests();
    
    List<ForgotPasswordRequestDto> getRequestsByStatus(ForgotPasswordStatus status);
    
    ForgotPasswordRequestDto getRequestById(Long id);
    
    ForgotPasswordRequestDto processRequest(Long id, ProcessForgotPasswordRequestDto data);
    
    List<ForgotPasswordRequestDto> getUserRequests(String email);
}