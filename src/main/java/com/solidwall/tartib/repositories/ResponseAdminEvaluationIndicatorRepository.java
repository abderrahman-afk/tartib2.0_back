package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ResponseAdminEvaluationIndicatorEntity;

@Repository
public interface ResponseAdminEvaluationIndicatorRepository extends JpaRepository<ResponseAdminEvaluationIndicatorEntity, Long> {
    List<ResponseAdminEvaluationIndicatorEntity> findByResponseAdminEvaluationCriteriaId(Long criteriaId);
}