package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.SecteurEntity;
@Repository
public interface SecteurRepository extends JpaRepository<SecteurEntity, Long> {
 

}
