package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.AdmissibilityResponseEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;

@Repository
public interface AdmissibilityResponseRepository extends JpaRepository<AdmissibilityResponseEntity, Long> {
    Optional<AdmissibilityResponseEntity> findTopByProjectIdentityOrderByCreatedAtDesc(
        ProjectIdentityEntity projectIdentity
    );
    long  countByAdmissibilityGridId(Long id);
}