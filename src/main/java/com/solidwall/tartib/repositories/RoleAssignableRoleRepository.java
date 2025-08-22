package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.RoleAssignableRoleEntity;
import com.solidwall.tartib.entities.RoleEntity;

import jakarta.transaction.Transactional;

@Repository
public interface RoleAssignableRoleRepository extends JpaRepository<RoleAssignableRoleEntity, Long> {
    List<RoleAssignableRoleEntity> findByRole(RoleEntity role);
    Optional<RoleAssignableRoleEntity> findByRoleAndAssignableRole(RoleEntity role, RoleEntity assignableRole);
    @Modifying
    @Transactional
    @Query("DELETE FROM RoleAssignableRoleEntity r WHERE r.role.id = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
    
    @Transactional
    void deleteByRole(RoleEntity role);
}