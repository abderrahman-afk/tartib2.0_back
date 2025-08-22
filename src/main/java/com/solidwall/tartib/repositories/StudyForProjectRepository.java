package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.solidwall.tartib.entities.StudyForProject;

import jakarta.transaction.Transactional;

public interface StudyForProjectRepository extends JpaRepository<StudyForProject, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM StudyForProject y WHERE y.projectStudy.id = :projectStudyId")
    void deleteByProjectStudyId(@Param("projectStudyId") Long projectStudyId);
}
