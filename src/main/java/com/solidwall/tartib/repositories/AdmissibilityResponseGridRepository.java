package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.AdmissibilityResponseGridEntity;

@Repository
public interface AdmissibilityResponseGridRepository extends JpaRepository<AdmissibilityResponseGridEntity, Long> {
    Optional<AdmissibilityResponseGridEntity> findByProjectId(Long projectId);
    @Query("SELECT r FROM AdmissibilityResponseGridEntity r WHERE r.project.id = :projectId ORDER BY r.createdAt DESC")
List<AdmissibilityResponseGridEntity> findByProjectIdOrderByCreatedAtDesc(@Param("projectId") Long projectId);
}
