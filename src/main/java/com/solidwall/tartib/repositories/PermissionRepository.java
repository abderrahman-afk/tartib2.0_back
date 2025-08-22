package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.solidwall.tartib.entities.PermissionEntity;

public interface PermissionRepository extends  JpaRepository<PermissionEntity, Long> {


}
