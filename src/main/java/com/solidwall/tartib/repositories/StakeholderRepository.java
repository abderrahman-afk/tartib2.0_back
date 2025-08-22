package com.solidwall.tartib.repositories;

import com.solidwall.tartib.entities.StakeHolderEntity;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StakeholderRepository extends JpaRepository<StakeHolderEntity, Long> {
        Optional<StakeHolderEntity> findByName(String name);

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM StakeHolderEntity y WHERE y.projectZone.id = :projectZoneId")
        void deleteByProjectZoneId(@Param("projectZoneId") Long projectZoneId);

}
