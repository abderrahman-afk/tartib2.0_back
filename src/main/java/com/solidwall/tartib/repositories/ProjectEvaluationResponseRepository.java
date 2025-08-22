package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectEvaluationResponseEntity;

@Repository
public interface ProjectEvaluationResponseRepository extends JpaRepository<ProjectEvaluationResponseEntity, Long> {
    Optional<ProjectEvaluationResponseEntity> findByProjectId(Long projectId);
    boolean existsByProjectId(Long projectId);
}