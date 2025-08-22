package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.solidwall.tartib.entities.RubriqueEntity;

import jakarta.transaction.Transactional;

public interface RubriqueRepository extends JpaRepository<RubriqueEntity, Long> {
    Optional<RubriqueEntity> findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM RubriqueEntity y WHERE y.projectPlan.id = :projectPlanId")
    void deleteByProjectPlanId(@Param("projectPlanId") Long projectPlanId);

}
