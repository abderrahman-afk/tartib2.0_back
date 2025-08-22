package com.solidwall.tartib.dto.user;

import com.solidwall.tartib.enums.ForgotPasswordStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessForgotPasswordRequestDto {
    
    @NotNull(message = "Status is required")
    private ForgotPasswordStatus status;
    
    private String adminNotes;
    private String newPassword; // Only for approved requests
}