package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ResponseAdminEvaluationCriteriaEntity;

@Repository
public interface ResponseAdminEvaluationCriteriaRepository extends JpaRepository<ResponseAdminEvaluationCriteriaEntity, Long> {
    List<ResponseAdminEvaluationCriteriaEntity> findByResponseAdminEvaluationComponentId(Long componentId);
}
