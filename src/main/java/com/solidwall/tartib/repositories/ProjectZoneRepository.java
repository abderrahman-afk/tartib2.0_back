package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectZoneEntity;

@Repository
public interface ProjectZoneRepository extends JpaRepository<ProjectZoneEntity, Long> {

   Optional<ProjectZoneEntity> findByProjectIdentity(ProjectIdentityEntity projectidentity);

         boolean existsByProjectIdentityId(Long projectId);
}
