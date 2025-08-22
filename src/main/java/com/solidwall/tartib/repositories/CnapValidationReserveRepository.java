package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.CnapValidationReserveEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CnapValidationReserveRepository  extends JpaRepository<CnapValidationReserveEntity, Long> {
    // Define any custom query methods if needed
    Optional<CnapValidationReserveEntity> findByProjectIdentity(ProjectIdentityEntity projectIdentity);
    Optional<CnapValidationReserveEntity> findByProjectIdentityId(Long projectIdentityId);

}
