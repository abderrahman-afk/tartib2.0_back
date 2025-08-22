package com.solidwall.tartib.implementations;

import com.solidwall.tartib.dao.AuthDao;
import com.solidwall.tartib.dto.auth.SigninDto;
import com.solidwall.tartib.entities.ArticleEntity;
import com.solidwall.tartib.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthImplementation {

  AuthDao signin(SigninDto signinDto, HttpServletRequest request);

  AuthDao signinWithToken(String token, jakarta.servlet.http.HttpServletRequest request);

  UserEntity signup();

  boolean signout(String token);
  public AuthDao refreshToken(String token, HttpServletRequest request);

  UserEntity forgotPassword();

  UserEntity resendCode(Long id);

  UserEntity verifyCode(ArticleEntity data);

  UserEntity resetPassword(Long id, ArticleEntity data);

}
