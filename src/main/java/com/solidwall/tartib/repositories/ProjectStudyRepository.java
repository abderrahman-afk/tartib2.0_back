package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectStudyEntity;

@Repository
public interface ProjectStudyRepository extends JpaRepository<ProjectStudyEntity, Long> {

   Optional<ProjectStudyEntity> findByProjectIdentity(ProjectIdentityEntity project);
}
