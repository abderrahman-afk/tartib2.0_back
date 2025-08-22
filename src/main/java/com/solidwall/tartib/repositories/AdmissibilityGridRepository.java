package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solidwall.tartib.entities.AdmissibilityGridEntity;
 

public interface AdmissibilityGridRepository extends JpaRepository<AdmissibilityGridEntity, Long> {
    Optional<AdmissibilityGridEntity> findByName(String name);
    Optional<AdmissibilityGridEntity> findByIsActive(boolean active);
    Optional<AdmissibilityGridEntity> findByIsActiveAndIsTemplate(boolean b, boolean c);

}
