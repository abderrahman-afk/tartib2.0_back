package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.AdmissibilityResponseCriteriaEntity;

@Repository
public interface AdmissibilityResponseCriteriaRepository extends JpaRepository<AdmissibilityResponseCriteriaEntity, Long> {
    List<AdmissibilityResponseCriteriaEntity> findByResponseGridId(Long responseGridId);
}