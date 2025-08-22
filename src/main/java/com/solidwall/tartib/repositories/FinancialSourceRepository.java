package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.FinancialSourceEntity;

import jakarta.transaction.Transactional;

import java.util.Optional;


@Repository
public interface FinancialSourceRepository extends JpaRepository<FinancialSourceEntity, Long> {
    Optional<FinancialSourceEntity> findByType(String type);
        @Transactional
        @Modifying
        @Query(value = "DELETE FROM FinancialSourceEntity y WHERE y.projectPlan.id = :projectPlanId")
        void deleteByProjectPlanId(@Param("projectPlanId") Long projectPlanId);
}
