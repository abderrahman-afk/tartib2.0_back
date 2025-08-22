package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.DerogationEntity;
import com.solidwall.tartib.enums.DerogationStatus;

@Repository
public interface DerogationRepository extends JpaRepository<DerogationEntity, Long> {
    List<DerogationEntity> findByProjectIdentityId(Long projectIdentityId);
    Optional<DerogationEntity> findByProjectIdentityIdAndAdmissibilityGridId(Long projectIdentityId, Long gridId);
    List<DerogationEntity> findByStatus(DerogationStatus status);
    List<DerogationEntity> findByRequestedBy(String requestedBy);
    List<DerogationEntity> findByDecidedBy(String decidedBy);
    Optional<DerogationEntity> findTopByProjectIdentityIdOrderByCreatedAtDesc(Long projectId);

}
