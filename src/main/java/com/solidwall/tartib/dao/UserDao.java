package com.solidwall.tartib.dao;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import com.solidwall.tartib.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDao {

  private Long id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  // Optional: private String password; // Excluded for security reasons
  private List<String> authorities;
  // add access and permissions to the user
  private List<String> access;
  private List<String> permission;
  private boolean isAccountNonExpired = true;
  private boolean accountNonLocked = true;
  private boolean credentialsNonExpired = true;
  private boolean enabled = true;

  public static UserDao fromUserEntity(UserEntity userEntity) {
    UserDao userDao = new UserDao();

    userDao.setId(userEntity.getId());
    userDao.setFirstname(userEntity.getFirstname());
    userDao.setLastname(userEntity.getLastname());
    userDao.setUsername(userEntity.getUsername());
    userDao.setEmail(userEntity.getEmail());
    // userDao.setPassword(userEntity.getPassword()); // Excluded for security
    // reasons
    userDao.setAuthorities(userEntity.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList()));
        // add accsess to the authorities
        userDao.setAccess(
          userEntity.getUserRoles().get(0).getRole().getRoleAccess()
          .stream()
          .map(roleAccess -> roleAccess.getAccess().getValue())
          .collect(Collectors.toList())
        );
        // add permissions to the authorities
        
    userDao.setPermission(
      userEntity.getUserRoles().get(0).getRole().getRolePermission()
      .stream()
      .map(rolePermission -> rolePermission.getPermission().getValue())
      .collect(Collectors.toList())
    );
    userDao.setAccountNonExpired(userEntity.isAccountNonExpired());
    userDao.setAccountNonLocked(userEntity.isAccountNonLocked());
    userDao.setCredentialsNonExpired(userEntity.isCredentialsNonExpired());
    userDao.setEnabled(userEntity.isEnabled());

    return userDao;
  }
}
