package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.PonderationComponentEntity;

@Repository
public interface PonderationComponentRepository extends JpaRepository<PonderationComponentEntity, Long> {
    List<PonderationComponentEntity> findByPonderationId(Long ponderationId);

}
