package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.ProjectEntity;


@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

     Optional<ProjectEntity> findBySimepCode(String simepCode);

}
