package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.entities.ProjectValidationEntity;

@Repository
public interface ProjectValidationRepository extends JpaRepository<ProjectValidationEntity, Long> {

  Optional<ProjectValidationEntity> findByProject(ProjectEntity project);

}
