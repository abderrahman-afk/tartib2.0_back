package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solidwall.tartib.entities.ProjectBlockEntity;

public interface projectBlockRepository extends JpaRepository<ProjectBlockEntity, Long> {
 
     Optional<ProjectBlockEntity> findByProjectId(Long projectId);
    ProjectBlockEntity findByIdAndProjectId(Long id, Long projectId);
 
}
