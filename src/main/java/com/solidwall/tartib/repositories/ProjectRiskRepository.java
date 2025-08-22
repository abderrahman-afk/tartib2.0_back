package com.solidwall.tartib.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectRiskEntity;

@Repository
public interface ProjectRiskRepository extends JpaRepository<ProjectRiskEntity, Long> {

    Optional<ProjectRiskEntity> findByProjectIdentity(ProjectIdentityEntity project);
    List<ProjectRiskEntity> findAllByProjectIdentity(ProjectIdentityEntity project);
    boolean existsByProjectIdentityId(Long projectId);

}
