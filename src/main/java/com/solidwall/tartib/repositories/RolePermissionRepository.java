package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.solidwall.tartib.entities.PermissionEntity;
import com.solidwall.tartib.entities.RolePermissionEntity;
import com.solidwall.tartib.entities.RoleEntity;

public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {

    @Query("SELECT rp FROM RolePermissionEntity rp WHERE rp.permission = :permission AND rp.role = :role")
    Optional<RolePermissionEntity> findByPermissionAndRole(@Param("permission") PermissionEntity permission, @Param("role") RoleEntity role);

}
