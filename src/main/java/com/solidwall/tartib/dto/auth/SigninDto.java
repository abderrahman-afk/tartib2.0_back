package com.solidwall.tartib.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninDto {

  @NotEmpty()
	// @Size(min = 3, max = 20)
	private String username;

  @NotEmpty()
	// @Size(min = 3, max = 20)
	private String password;

}
