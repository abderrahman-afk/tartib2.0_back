package com.solidwall.tartib.dao;

import com.solidwall.tartib.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDao {

  private UserDao user;
  private String token;

  public static AuthDao fromUserEntity(UserEntity userEntity, String token) {
    AuthDao authDao = new AuthDao();

    authDao.setToken(token);
    authDao.setUser(UserDao.fromUserEntity(userEntity)); // Using UserDao for user details

    return authDao;
  }
}