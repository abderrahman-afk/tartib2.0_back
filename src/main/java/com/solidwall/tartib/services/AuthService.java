package com.solidwall.tartib.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.configs.CustomUserDetailsService;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.core.utils.JwtUtil;
import com.solidwall.tartib.dao.AuthDao;
import com.solidwall.tartib.dto.auth.SigninDto;
import com.solidwall.tartib.entities.ArticleEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.implementations.AuthImplementation;
import com.solidwall.tartib.repositories.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService implements AuthImplementation {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  JwtUtil jwtUtil;

  @Autowired
  CustomUserDetailsService customUserDetailsService;

  @Override
  public AuthDao signin(SigninDto signinDto, HttpServletRequest request) {
    Optional<UserEntity> user = userRepository.findByUsername(signinDto.getUsername());
    if (user.isPresent()) {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(signinDto.getUsername(), signinDto.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String clientIp = request.getHeader("X-Forwarded-For");
      if (clientIp == null || clientIp.isEmpty()) {
        clientIp = request.getRemoteAddr();
      }
      UserEntity userEntity = user.get();
      String lastIp = userEntity.getLastIp();
      System.out.println("[DEBUG] AuthService - signin: lastIp=" + lastIp + ", clientIp=" + clientIp);
      if (lastIp != null && !lastIp.equals(clientIp)) {
        System.out.println("[DEBUG] AuthService - signin: IP mismatch, denying login.");
        throw new org.springframework.security.access.AccessDeniedException("Utilisateur déja connecté depuis une autre Machine " );
      }
      userEntity.setLastIp(clientIp);
      userRepository.save(userEntity);
      System.out.println("[DEBUG] AuthService - signin: Updated lastIp to " + clientIp);
      String accessToken = jwtUtil.generateToken(authentication.getName(), clientIp);
      System.out.println("[DEBUG] AuthService - Generated JWT: " + accessToken);
      AuthDao authDao = AuthDao.fromUserEntity(userEntity, accessToken);
      return authDao;
    } else {
      throw new NotFoundException("Les identifications sont erronées");
    }
  }

  @Override
  public UserEntity signup() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'signup'");
  }

  @Override
  public UserEntity forgotPassword() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'forgotPassword'");
  }

  @Override
  public UserEntity resendCode(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'resendCode'");
  }

  @Override
  public UserEntity verifyCode(ArticleEntity user) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'verifyCode'");
  }

  @Override
  public UserEntity resetPassword(Long id, ArticleEntity user) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
  }

  @Override
  public AuthDao signinWithToken(String token, HttpServletRequest request) {
    String username = jwtUtil.extractUsername(token);
    UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
    if (jwtUtil.validateToken(token, userDetails.getUsername())) {
      Optional<UserEntity> user = userRepository.findByUsername(userDetails.getUsername());
      if (user.isPresent()) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
          clientIp = request.getRemoteAddr();
        }
        UserEntity userEntity = user.get();
        String lastIp = userEntity.getLastIp();
        System.out.println("[DEBUG] AuthService - signinWithToken: lastIp=" + lastIp + ", clientIp=" + clientIp);
        if (lastIp != null && !lastIp.equals(clientIp)) {
          System.out.println("[DEBUG] AuthService - signinWithToken: IP mismatch, denying login.");
          throw new org.springframework.security.access.AccessDeniedException("User already logged in from another IP: " + lastIp);
        }
        userEntity.setLastIp(clientIp);
        userRepository.save(userEntity);
        System.out.println("[DEBUG] AuthService - signinWithToken: Updated lastIp to " + clientIp);
        String tokenIp = jwtUtil.extractIp(token);
        System.out.println("[DEBUG] AuthService - Token IP (signinWithToken): " + tokenIp);
        if (tokenIp != null && !tokenIp.equals(clientIp)) {
          System.out.println("[DEBUG] AuthService - IP address mismatch in signinWithToken. Returning unauthorized.");
          throw new org.springframework.security.access.AccessDeniedException("IP address mismatch");
        }
        String accessToken = jwtUtil.generateToken(userEntity.getUsername(), clientIp);
        System.out.println("[DEBUG] AuthService - Generated JWT (signinWithToken): " + accessToken);
        AuthDao authDao = AuthDao.fromUserEntity(userEntity, accessToken);
        return authDao;
      } else {
        throw new NotFoundException("user not found");
      }
    } else {
      throw new NotFoundException("user not found");
    }
  }

  
 @Override
  public boolean signout(String token) {
    try {
      String username = jwtUtil.extractUsername(token);
      Optional<UserEntity> user = userRepository.findByUsername(username);
      if (user.isPresent()) {
        UserEntity userEntity = user.get();
        userEntity.setLastIp(null);
        userRepository.save(userEntity);
        System.out.println("signout: Cleared lastIp for user " + username);
        return true;
      }
      return false;
    } catch (Exception e) {
      System.out.println("AuthService - signout: Error " + e.getMessage());
      return false;
    }
  }
 @Override
  public AuthDao refreshToken(String token, HttpServletRequest request) {
      try {
          String username = jwtUtil.extractUsername(token);
          UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

          if (jwtUtil.validateToken(token, userDetails.getUsername())) {
              Optional<UserEntity> user = userRepository.findByUsername(username);
              if (user.isPresent()) {
                  String clientIp = request.getHeader("X-Forwarded-For");
                  if (clientIp == null || clientIp.isEmpty()) {
                      clientIp = request.getRemoteAddr();
                  }

                  UserEntity userEntity = user.get();
                  // Verify IP matches stored IP and token IP
                  String tokenIp = jwtUtil.extractIp(token);
                  if (tokenIp != null && !tokenIp.equals(clientIp)) {
                      throw new org.springframework.security.access.AccessDeniedException("IP address mismatch during refresh");
                  }

                  if (userEntity.getLastIp() != null && !userEntity.getLastIp().equals(clientIp)) {
                      throw new org.springframework.security.access.AccessDeniedException("User session moved to different IP");
                  }

                  String newAccessToken = jwtUtil.generateToken(username, clientIp);
                  System.out.println("[DEBUG] AuthService - Token refreshed for user: " + username);
                  return AuthDao.fromUserEntity(userEntity, newAccessToken);
              }
          }
          throw new NotFoundException("Invalid token for refresh");
      } catch (Exception e) {
          System.out.println("[DEBUG] AuthService - refresh token error: " + e.getMessage());
          throw e;
      }
  }
}
