package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.StudyStateEntity;

@Repository
public interface StudyStateRepository extends JpaRepository<StudyStateEntity, Long> {

    Optional<StudyStateEntity> findByName(String name);
}
