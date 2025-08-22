package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ComponentLogicEntity;

import jakarta.transaction.Transactional;

import java.util.Optional;


@Repository
public interface ComponentLogicRepository extends JpaRepository<ComponentLogicEntity, Long>{
    Optional<ComponentLogicEntity> findByName(String name);

                @Transactional
        @Modifying
        @Query(value = "DELETE FROM ComponentLogicEntity y WHERE y.projectLogic.id = :projectLogicId")
        void deleteByProjectLogicId(@Param("projectLogicId") Long projectLogicId);
}
