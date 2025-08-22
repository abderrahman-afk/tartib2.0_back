package com.solidwall.tartib.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.solidwall.tartib.entities.AutorisationEntity;

import jakarta.transaction.Transactional;


public interface AutorisationRepository extends JpaRepository<AutorisationEntity, Long>  {

    Optional<AutorisationEntity> findByName(String name);

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM AutorisationEntity y WHERE y.projectStudy.id = :projectStudyId")
        void deleteByProjectStudyId(@Param("projectStudyId") Long projectStudyId);

}
