package com.solidwall.tartib.dto;

import java.util.List;
import java.util.Set;

import com.solidwall.tartib.enums.MinistryAccessType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {

 private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    
    // Ministry fields
    private Long ministryId;
    private List<Long> referenceMinistryIds;
    private MinistryAccessType ministryAccessType = MinistryAccessType.OWN_MINISTRY;
}
