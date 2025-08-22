package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectLogicEntity;
import java.util.Optional;


public interface ProjectLogicRepository   extends JpaRepository<ProjectLogicEntity, Long>  {
    Optional<ProjectLogicEntity> findByProjectIdentity(ProjectIdentityEntity projectIdentity);

    boolean existsByProjectIdentityId(Long projectId);
}
