package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.solidwall.tartib.entities.AccessEntity;
import com.solidwall.tartib.entities.RoleAccessEntity;
import com.solidwall.tartib.entities.RoleEntity;

public interface RoleAccessRepository extends  JpaRepository<RoleAccessEntity, Long> {

    @Query("SELECT ra FROM RoleAccessEntity ra WHERE ra.access = :access AND ra.role = :role")
    Optional<RoleAccessEntity> findByAccessAndRole(@Param("access") AccessEntity access, @Param("role") RoleEntity role);
        // **ADD THIS METHOD**
    List<RoleAccessEntity> findByRole(RoleEntity role);
    
    // Optional: You could also add this for more explicit deletion
    @Modifying
    @Query("DELETE FROM RoleAccessEntity ra WHERE ra.role = :role")
    void deleteByRole(@Param("role") RoleEntity role);
}
