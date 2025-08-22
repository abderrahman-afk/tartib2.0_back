package com.solidwall.tartib.repositories.interfaces;

import java.util.Optional;

import com.solidwall.tartib.dto.roleAccess.FindDto;
import com.solidwall.tartib.entities.RoleAccessEntity;

public interface RoleAccessRepositoryInterface {

    Optional<RoleAccessEntity> findOneByCriteria(FindDto data);
    
}
