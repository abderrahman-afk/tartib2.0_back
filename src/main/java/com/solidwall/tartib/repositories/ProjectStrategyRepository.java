package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectStrategyEntity;

@Repository
public interface ProjectStrategyRepository extends JpaRepository<ProjectStrategyEntity, Long> {

  Optional<ProjectStrategyEntity> findByProjectIdentity(ProjectIdentityEntity projectIdentity);

boolean existsByProjectIdentityId(Long projectId);
}
