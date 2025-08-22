package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.enums.ProjectStaut;

@Repository
public interface ProjectIdentityRepository extends JpaRepository<ProjectIdentityEntity, Long> {

  Optional<ProjectIdentityEntity> findByCode(String code);

  List<ProjectIdentityEntity> findByStatut(String statut); // To find projects by their status

  long count(); // To count existing projects for sequential numbering

  List<ProjectIdentityEntity> findByStatut(ProjectStaut statut);
}
