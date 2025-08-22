package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solidwall.tartib.entities.AdminEvaluationResponseEntity;

import java.util.Optional;
public interface AdminEvaluationResponseRepository extends JpaRepository<AdminEvaluationResponseEntity, Long> {
    Optional<AdminEvaluationResponseEntity> findByProjectId(Long projectId);
    boolean existsByProjectId(Long projectId);
}
