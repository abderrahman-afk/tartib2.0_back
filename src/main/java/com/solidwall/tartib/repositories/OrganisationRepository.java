package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.OrganisationEntity;

@Repository
public interface OrganisationRepository extends JpaRepository<OrganisationEntity, Long> {

    Optional<OrganisationEntity> findByName(String name);

}
