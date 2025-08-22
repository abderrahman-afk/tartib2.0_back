package com.solidwall.tartib.dto.user;

import java.util.Date;

import com.solidwall.tartib.enums.ForgotPasswordStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequestDto {
    private Long id;
    private String email;
    private String userFirstname;
    private String userLastname;
    private String username;
    private ForgotPasswordStatus status;
    private String processedBy;
    private String adminNotes;
    private Date createdAt;
    private Date updatedAt;
}