package com.solidwall.tartib.repositories;

import com.solidwall.tartib.entities.LinkedProjectEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface LinkedProjectRepository  extends JpaRepository<LinkedProjectEntity, Long>{
    Optional<LinkedProjectEntity> findByProjectCode(String projectCode);
            @Transactional
        @Modifying
        @Query(value = "DELETE FROM LinkedProjectEntity y WHERE y.projectZone.id = :projectZoneId")
        void deleteByProjectZoneId(@Param("projectZoneId") Long projectZoneId);
}
