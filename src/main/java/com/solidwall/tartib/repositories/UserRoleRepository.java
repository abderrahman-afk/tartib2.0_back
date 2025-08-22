package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.RoleEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.entities.UserRoleEntity;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long>{

  
  @Query("SELECT ur FROM UserRoleEntity ur WHERE ur.user = :user AND ur.role = :role")
  Optional<UserRoleEntity> findByUserAndRole(@Param("user") UserEntity user, @Param("role") RoleEntity role);
   List<UserRoleEntity> findByUser(UserEntity user); // Add this method

}
