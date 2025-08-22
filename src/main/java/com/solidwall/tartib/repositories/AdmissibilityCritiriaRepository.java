package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import com.solidwall.tartib.entities.AdmissibilityCriteriaEntity;

import jakarta.transaction.Transactional;

public interface AdmissibilityCritiriaRepository extends JpaRepository<AdmissibilityCriteriaEntity, Long> {
    Optional<AdmissibilityCriteriaEntity> findByName(String name);

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM AdmissibilityCriteriaEntity y WHERE y.admissibiltyGrid.id = :admissibiltyGridId")
        void deleteByAdmissibiltyGridId(@Param("admissibiltyGridId") Long admissibiltyGridId);

}