package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import java.util.List;


@Repository
public interface ProjectPlanRepository extends JpaRepository<ProjectPlanEntity, Long> {

  Optional<ProjectPlanEntity> findByProjectIdentity(ProjectIdentityEntity project);

boolean existsByProjectIdentityId(Long projectId);

}
